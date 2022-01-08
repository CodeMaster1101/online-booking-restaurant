package com.mile.pc.mile.restoraunt.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;
import com.mile.pc.mile.restoraunt.app.model.BusyReservation;
import com.mile.pc.mile.restoraunt.app.model.CustomTable;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.CurrentDeployedReservations;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

import lombok.SneakyThrows;

/**
 * The semi-Administrator service which is responsible for the waiter in the locale.
 * It's job is monitoring the changes to the reservations and the tables in the locale.
 * It can perform the following actions: telling the DB that a user/guest has just arrived on the their reservation according
 * to the time of the reservation; Telling the DB that a guest/client has just called for check and is ready to leave based on the table respectively
 * 
 * @author Mile Stanislavov
 * 
 */
@Service @Transactional 
public class WaiterService {

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
	@SneakyThrows
	public void setBusy(long tableid) {
		CustomTable table = tRepo.findById(tableid).get();
		if(table.getBusy()!=true) {
			Reservation currentRes = findCurrentReservation(table);
			if(currentRes == null)
				throw new NullPointerException("reservation not found");
			//keeps the reservation as a "busy reservation" in a map to ease the complexity, later implemented in the setCalm(long id) method
			currentRes.setLivingReservation(currentReservations.save(new BusyReservation(null, currentRes)));
			userArrived(currentRes.getId());
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
	public void setCalm(long tableid) {
		CustomTable table = tRepo.findById(tableid).get();
		BusyReservation currentReservation = currentReservations.findAll().stream()
				.filter(res -> res.getReservation().getTable().equals(table)).findFirst().get();
		emptyTableAndReservations(table, currentReservation);
	}

	/**
	 * Sets the current table as a busy table, but without a user, rather with a guest.
	 * the guest is someone who just walked in the restoraunt and has no idea that he can reserve a table, 
	 * or they just don't want to go through the process of reserving a table.
	 * The algorithm calls the main service which has a protected method for comparing two reservations, in this scenario we want to create
	 * a nonUser or rather a guest reservation that will be specifically long 4 hours and compare the time intervals with the others on this table.
	 * this reservation won't be having a user. So there wont be any need of refunding processes.
	 * @param id the identifier for the table object
	 */
	public void setGuestBusy(long id) {
		CustomTable table = tRepo.findById(id).get();
		Reservation reservation = CONSTANTS.GUEST_RESERVATION;
		reservation.getGuest().setReservation(reservation);
		main_S.checkOtherReservations(table, reservation);
		currentReservations.save(new BusyReservation(null, reservation));
		tRepo.save(table);
	}
	/**
	 * 
	 * @return the reservations for today
	 */
	public List<Reservation> todayReservations(){
		return reservations.findAll().stream()
				.filter(r -> r.getTime().getDayOfMonth() == LocalDate.now().getDayOfMonth())
				.collect(Collectors.toList());
	}
	/**
	 * removes the reservation via the User referenced by the reservation with orphan removal
	 * @param id
	 */
	public void removeReservation(long id) {
		reservations.findById(id).get().getUser().setReservation(null);
	}

	//PRIVATE HELPING METHODS

	/**
	 * checks whether a user is on time or not.
	 * Conditions -> 
	 * - the current time must be after the reservation time minus some minutes referenced by CONSTANTS.BEFORE_RESERVATION_TIME
	 *      this means that the user can come earlier to their reservation by a certain amount of time. If he comes to early, well... no refund.
	 * -the current time must be before the reservation time plus some minutes referenced by CONSTANTS.AFTER_RESERVATION_TIME  
	 * 		this means that the user can come later, more precisely, the user can be late by a certain amount of minutes. 
	 * @param reservationId the id that fetches the reservation object
	 * @return true if all the testing is correct
	 */
	private boolean checkUserOnTime(long reservationId) {
		Reservation reservation = reservations.findById(reservationId).get();
		LocalTime now = LocalTime.now();
		User user = reservation.getUser();
		if(!(now.isAfter(user.getReservation().getTime().toLocalTime().minusMinutes(CONSTANTS.BEFORE_RESERVATION_TIME)) &&
				now.isBefore(user.getReservation().getTime().toLocalTime().plusMinutes(CONSTANTS.AFTER_RESERVATION_TIME)))) {
			return false;
		}return true;
	}
	/**
	 * identifies that the user has arrived at the restoraunt, checks if the user is on time for their reservation.
	 * if true, then the fee that the user gave to reserve a table is returned to him. 
	 * @param reservationId
	 */
	@SneakyThrows
	private void userArrived(long reservationId) {
		if(checkUserOnTime(reservationId)) {
			User user = reservations.findById(reservationId).get().getUser();
			user.setBalance(user.getBalance() + user.getReservation().getFee());
		}
	}
	/**
	 * Probably the most weird algorithm, but... since the comparing is done by hours, this is made possible.
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
	 * Cleans the reservation in two ways depending if it's a User based reservation
	 * or a Guest based reservation. No matter the way, the cleaning is done via reference removal.
	 * @param table 
	 * @param currentReservation
	 */
	private void emptyTableAndReservations(CustomTable table, BusyReservation currentReservation) {
		if(currentReservation.getReservation().getUser() == null)
			currentReservation.getReservation().getGuest().setReservation(null);
		else
			removeReservation(currentReservation.getReservation().getId());

		table.setBusy(false);
	}

}
