package com.mile.pc.mile.restoraunt.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;
import com.mile.pc.mile.restoraunt.app.model.BusyReservation;
import com.mile.pc.mile.restoraunt.app.model.CustomTable;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.CurrentDeployedReservations;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.RoleRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

import lombok.SneakyThrows;

/**
 * The one-half administrator service which is responsible for the waiter in the locale.
 * It's job is monitoring the changes to the reservations and the tables in the locale.
 * It can perform the following actions: telling the DB that a user/guest has just arrived on the their reservation according
 * to the time of the reservation; Telling the DB that a guest/client has just called for check and is ready to leave based on the table respectively
 * 
 * @author Mile Stanislavov
 * 
 */
@Service  
public class WaiterService {

	@Autowired RoleRepository roleRepo;
	@Autowired CustomTableRepository tRepo;
	@Autowired ReservationRepository reservations;
	@Autowired UserRepository urepo;
	@Autowired CurrentDeployedReservations currentReservations;
	@Autowired MainService main_S;

	/**
	 * Sets the table to busy, in other words, the table says that someone is currently sitting on that table.
	 * checks if the table is already busy, if it's busy, then an exception occurs. Tries to find
	 * the closest reservation which will probably be the user that has just arrived. But does not guarantee.
	 * Also runs a few tests to see if the user arrived within the time range or not. Lastly, sets the table to a "busy table".
	 * @param tableid, the id used to fetch the table object
	 */
	@SneakyThrows @Transactional
	public void setBusy(long tableid) {
		CustomTable table = tRepo.findById(tableid).get();
		if(table.getBusy()!=true) {
			Reservation currentRes = findCurrentReservation(table);
			if(currentRes == null)
				throw new NullPointerException("reservation not found");
			reservationBusyLogic(currentRes);
			table.setBusy(true);
			return;
		}throw new Exception("already busy");
	}
	
	/**
	 * Clarifies that on this table nobody is sitting, or to be more precise, 
	 * the current client/guest had called for check and is ready to leave.
	 * Filters through all currentReservations of all the tables, but there can be only one busy reservation for each table.
	 * Fetches the busy reservation referencing the table and does some cleaning logic.
	 * @param tableid the id that identifies the table object
	 */
	@Transactional
	public void setCalm(long tableid) {
		CustomTable table = tRepo.findById(tableid).get();
		BusyReservation currentReservation = currentReservations.findAll().stream()
				.filter(res -> res.getReservation().getTable().equals(table)).findFirst().get();
		emptyTableAndReservations(table, currentReservation);
	}

	/**
	 * Sets the current table as a busy table, but without a user, rather with a guest.
	 * the guest is someone who just walked in the restaurant and has no idea that he can reserve a table, 
	 * or they just don't want to go through the process of reserving a table.
	 * The algorithm calls the main service which has a protected method for comparing two reservations, in this scenario we want to create
	 * a nonUser or rather a guest reservation that will be specifically long 4 hours and compare the time intervals with the others on this table.
	 * this reservation won't be having a user. So there wont be any need of refunding processes.
	 * The procedure is standard regarding the Non-User oriented reservation requirements.
	 * @see MainService
	 * @param id the identifier for the table object
	 */
	@SneakyThrows @Transactional
	public void setGuestBusy(long id) {
		CustomTable t = tRepo.findById(id).get();
		if(t.getBusy()) return;
		Reservation reservation = reservations.save(new Reservation(null,true,null,null,LocalDateTime.now(), LocalDateTime.now().plusHours(4),null, null));
		if(!checkGuestArrival(reservation)) return;
		reservation.setUTable(t);	
		main_S.checkOtherReservations(t, reservation);
		currentReservations.save(new BusyReservation(null, reservation));
		t.setBusy(true);
	}
	/**
	 * @return the reservations for today -> every reservation which LocalDate is the current Date
	 */
	public List<Reservation> todayReservations(){
		return reservations.findAll().stream()
				.filter(r -> r.getTime().getDayOfMonth() == LocalDate.now().getDayOfMonth())
				.collect(Collectors.toList());
	}
	
	/**
	 * Removes every reservation that has expired, in other words, 
	 * every reservation that has been canceled but nobody has arrived or the user hadn't shown up on time.
	 * Also sends all the fee to the Administrator from every reservation that their respective user hasn't attended to.
	 */
	@Transactional
	public void removeExpiredReservations() {
		List<Reservation> expiredReservations = reservations.findAll().stream()
				.filter(r -> r.getTime().isBefore(LocalDateTime.now().minusMinutes(CONSTANTS.AFTER_RESERVATION_TIME)) 
						&& r.getLivingReservation() == null)
				.collect(Collectors.toList());
		if(expiredReservations.isEmpty() == false) {
			for (Reservation reservation : expiredReservations) {
				sendMoneyToAdmin(reservation);
			}
		}
	}
	//PRIVATE HELPING METHODS

	/** Checks if the new guest is over the time limit. For example, if the guest arrives in 22:00. 
	 * Then he wont be able to sit.
	 * @param reservation
	 * @return true if the guest arrived before 22:00
	 */
	private boolean checkGuestArrival(Reservation reservation) {
		if(reservation.getTime().isAfter(LocalDateTime.of(LocalDate.now(), CONSTANTS.END)))
			return false;
		return true;
	}
	
	/**
	 * Cleans the reservation in two ways depending if it's a User based reservation
	 * or a Guest based reservation. 
	 * The reservation instance is cut off from every referenced entity, and so it is not dependent to any object 
	 * and can be removed from the DB
	 * @param id
	 */
	@Transactional
	private void removeReservation(long id) {
		Reservation reservation = reservations.findById(id).get();
		if(reservation.getUser() != null) {
			reservation.getUser().setReservation(null);
			reservation.getUser().setReservationMoment(null);
		}	
		else {
		reservation.getTable().removeReservation(reservation);
		reservations.deleteById(id);
		}
	}
	
	/**
	 * Deletes the reservation and sends the money to the administrator from the reservation 
	 * that hasn't been attended to. Removes the reservation time from the user
	 * @param reservation
	 */
	@Transactional
	protected void sendMoneyToAdmin(Reservation reservation) {
		if(reservation.getFee() != null) {
			User admin = urepo.findAll().stream().filter(u -> u.getRoles().contains(roleRepo.findByType("ADMIN"))).findFirst().get();
			admin.setBalance(admin.getBalance() + reservation.getFee());
		}
		removeReservation(reservation.getId());
	}
	
	/**
	 * Since the comparing is done by hours, this is made possible.
	 * The algorithm checks the following relation: x -> y -> z. 
	 * (reservation time - minutes) -> (current time) -> (reservation time + minutes)
	 * Procedure ->
	 * - checks that every reservation must be of the current day.
	 * - the current time must be after the reservation time minus a couple of minutes, 
	 *       referenced by CONSTANTS.BEFORE_RESERVATION_TIME, almost an hour.
	 * - the current time bust be before the reservation time plus a couple of minutes,
	 * referenced by CONSTANTS.AFTER_RESERVATION_TIME.
	 * @param table
	 * @return the reservation if found, otherwise null
	 */
	private Reservation findCurrentReservation(CustomTable table) {
		LocalTime now = LocalTime.now();
		return table.getReservations().stream()
				.filter(r -> 
				r.getTime().getDayOfMonth() == LocalDateTime.now().getDayOfMonth() &&
				now.isAfter(r.getTime().toLocalTime().minusMinutes(CONSTANTS.BEFORE_RESERVATION_TIME))
				&&  now.isBefore(r.getTime().toLocalTime().plusMinutes((CONSTANTS.AFTER_RESERVATION_TIME)))).findFirst().get();
	}
	
	/**
	 * Removes the reservation from the DB. Set the table to available(busy == null)
	 * This method is called to clean up the current user sitting on the table and his reservation.
	 * Checks if the table is full calling the main service where the method lives.
	 * @param table 
	 * @param currentReservation
	 */
	private void emptyTableAndReservations(CustomTable table, BusyReservation currentReservation) {
		removeReservation(currentReservation.getReservation().getId());		
		table.setBusy(false);
	}
	
	/**
	 * Creates a "busy reservation" later to be fetched by the @see setCalm(long id).
	 * Returns the user's money from the reservation fee. Sets the fee to null.
	 * @param reservation
	 */
	@Transactional
	private void reservationBusyLogic(Reservation reservation) {
		//keeps the reservation as a "busy reservation" in a map to ease the complexity, later implemented in the setCalm(long id) method
		reservation.setLivingReservation(currentReservations.save(new BusyReservation(null, reservation)));
		reservation.getUser().setBalance(reservation.getUser().getBalance() + reservation.getFee());
		reservation.setFee(null);
	}

}
