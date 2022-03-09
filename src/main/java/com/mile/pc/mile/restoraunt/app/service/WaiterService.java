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
import com.mile.pc.mile.restoraunt.app.exceptions.AlreadyBusyException;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.RestorauntTable;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.RoleRepository;
import com.mile.pc.mile.restoraunt.app.repo.TableRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

import lombok.SneakyThrows;

/**
 This section follows the actions of the "waiter" - the person responsible for the reservations in the local place(restaurant).
 The waiter keeps track of who arrives and whether they have a reservation or not. Which table they sit on, etc...
 Functionalities: 
 - View of all the tables in the restaurant (ID, Occupied/Not, Actions). 
 - View of today reservations.
 - Removing all "expired reservations" -> Each reservation that hasn't been attended to is marked as an expired reservation, which the waiter can remove. While doing so
   the fee from every reservation is being transfered to the admin's wallet and the user on that reservation does not get the money.
 - Actions -> busy/calm

 * @author Mile Stanislavov
 *
 */
@Service  
public class WaiterService {

	@Autowired RoleRepository roleRepo;
	@Autowired TableRepository tRepo;
	@Autowired ReservationRepository reservations;
	@Autowired UserRepository urepo;

	/**
	 * Informs the service that a user with a reservation has just arrived. The service looks for the current reservation
	   and sets that table to occupied with the current user that has just arrived. 
	   If the service does not return a reservation, the user is either late
	   (the user has arrived after 50 minutes from his original reservation time), or the user is too early 
	   (50 minutes before his reservation time).
	   sets the table to busy, as well as the reservation
	 * @param tableid - the id used to find the specific table from the repository
	 */
	@SneakyThrows @Transactional
	public void setBusy(long tableid) {
		RestorauntTable table = tRepo.findById(tableid).get();
		if(table.getBusy()!=true) {
			Reservation currentRes = findCurrentReservation(table);
			reservationBusyLogic(currentRes);
			table.setBusy(true);
			return;
		}
		throw new AlreadyBusyException(tableid);
	}

	/**
	 * Informs the service that the current client has called for check or has gotten up from the table. This is crucial as one 
	   table must be empty in order for another client to sit on that table.
	 * @param tableid
	 */
	@Transactional
	public void setCalm(long tableid) {
		RestorauntTable table = tRepo.findById(tableid).get();
		emptyTableAndReservations(table, reservations.findAll().stream()
				.filter(res -> res.getTable().equals(table) && res.isBusy()).findFirst().get());
	}

	public List<Reservation> todayReservations(){
		return reservations.findAll().stream()
				.filter(r -> r.getTime().getDayOfMonth() == LocalDate.now().getDayOfMonth())
				.collect(Collectors.toList());
	}

	@Transactional
	public int removeExpiredReservations() {
		int i = 0;
		List<Reservation> expiredReservations = reservations.findAll().stream()
				.filter(r ->  r.isBusy() == false && r.getTime().plusMinutes(CONSTANTS.AFTER_RESERVATION_TIME).isBefore(LocalDateTime.now())).collect(Collectors.toList());
		if(expiredReservations.isEmpty() == false) {
			for (Reservation reservation : expiredReservations) {
				sendMoneyToAdmin(reservation);
				i++;
			}
		}return i;
	}

	private void removeReservation(long id) {
		Reservation reservation = reservations.findById(id).get();
		reservation.getUser().setReservation(null);
		reservation.getUser().setReservationMoment(null);
		reservation.getTable().removeReservation(reservation);
	}

	private void sendMoneyToAdmin(Reservation reservation) {
		if(reservation.getFee() != null) {
			User admin = urepo.findAll().stream().filter(u -> u.getRoles().contains(
					roleRepo.findByType("ADMIN"))).findFirst().get();
			admin.setBalance(admin.getBalance() + reservation.getFee());
		}
		removeReservation(reservation.getId());
	}

	private Reservation findCurrentReservation(RestorauntTable table) {
		LocalTime now = LocalTime.now();
		return table.getReservations().stream()
				.filter(r -> 
				r.getTime().getDayOfMonth() == LocalDateTime.now().getDayOfMonth() &&
				now.isAfter(r.getTime().toLocalTime().minusMinutes(CONSTANTS.BEFORE_RESERVATION_TIME))
				&&  now.isBefore(r.getTime().toLocalTime().plusMinutes((CONSTANTS.AFTER_RESERVATION_TIME)))).findFirst().get();
	}

	private void emptyTableAndReservations(RestorauntTable table, Reservation currentReservation) {
		removeReservation(currentReservation.getId());		
		table.setBusy(false);
	}

	private void reservationBusyLogic(Reservation reservation) {
		//keeps the reservation as a "busy reservation" in a map to ease the complexity, later implemented in the setCalm(long id) method
		reservation.getUser().setBalance(reservation.getUser().getBalance() + reservation.getFee());
		reservation.setFee(null);
		reservation.setBusy(true);

	}

}
