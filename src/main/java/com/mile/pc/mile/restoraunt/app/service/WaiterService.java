package com.mile.pc.mile.restoraunt.app.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

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

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@Service @Transactional @NoArgsConstructor
public class WaiterService {

	@Autowired CustomTableRepository tRepo;
	@Autowired ReservationRepository reservations;
	@Autowired UserRepository urepo;
	@Autowired CurrentDeployedReservations currentReservations;

	@SneakyThrows
	public void setBusy(long tableid) {
		CustomTable table = tRepo.findById(tableid).get();
		if(table.getBusy()!=true) {
			Reservation currentRes = findCurrentReservation(table);
			currentReservations.save(new BusyReservation(null, currentRes));
			userArrived(currentRes.getId());
			table.setBusy(true);
			return;
		}
	}
	public void setCalm(long tableid) {
		CustomTable table = tRepo.findById(tableid).get();
		BusyReservation currentReservation = currentReservations.findAll().stream()
				.filter(res -> res.getReservation().getTable().equals(table)).findFirst().get();
		emptyTableAndReservations(table, currentReservation);

	}
	@SneakyThrows
	public void setGuestOnTable(long tableid) {
		CustomTable table = tRepo.findById(tableid).get();
		if(table.getBusy() == true) {
			throw new Exception("busy");
		}
		if(check2hAhead(table) != null)
			throw new Exception("already a reservation in 2h radius");
		Reservation proxyReservation = reservations.save(new Reservation());
		proxyReservation.getGuest().setReservation(proxyReservation);
		Reservation currentRes = findCurrentReservation(table);
		currentReservations.save(new BusyReservation(null, currentRes));
		table.setBusy(true);
	}
	//PRIVATE HELPING METHODS
	private boolean checkUserOnTime(long reservationId) {
		Reservation reservation = reservations.findById(reservationId).get();
		LocalTime now = LocalTime.now();
		User user = reservation.getUser();
		if(!(now.isAfter(OffsetDateTime.parse(user.getReservation().getTime().toString()).toLocalTime().minusMinutes(20)) &&
				now.isBefore(OffsetDateTime.parse(user.getReservation().getTime().toString()).toLocalTime().plusMinutes(CONSTANTS.afterReservationTime)))) {
			return false;
		}return true;
	}
	@SneakyThrows
	private void userArrived(long reservationId) {
		if(checkUserOnTime(reservationId)) {
			User user = reservations.findById(reservationId).get().getUser();
			user.setBalance(user.getBalance() + CONSTANTS.fee);
		}
	}
	private Reservation findCurrentReservation(CustomTable table) {
		LocalTime now = LocalTime.now();
		return table.getReservations().stream()
				.filter(r -> r.getTime().getDayOfMonth() == LocalDateTime.now().getDayOfMonth() 
				&& OffsetDateTime.parse(r.getTime().toString()).toLocalTime().isAfter(now.minusMinutes(20)) 
				&&  OffsetDateTime.parse(r.getTime().toString()).toLocalTime().isBefore(now.plusMinutes(CONSTANTS.afterReservationTime).minusMinutes(10))).findFirst().get();
	}
	private void emptyTableAndReservations(CustomTable table, BusyReservation currentReservation) {
		table.getReservations().remove(currentReservation.getReservation());
		reservations.delete(currentReservation.getReservation());
		currentReservations.deleteById(currentReservation.getReservation().getId());
		//if(currentReservation.getReservation().getGuest() == null)
		//table.setBusy(false);
	}
	private Reservation check2hAhead(CustomTable table) {
		LocalTime now = LocalTime.now();
		return
				table.getReservations().stream().filter(r -> now.plusHours(3).isAfter(OffsetDateTime.parse(r.getTime().toString()).toLocalTime()) 
						&& OffsetDateTime.parse(r.getTime().toString()).toLocalTime().isAfter(now.minusMinutes(CONSTANTS.afterReservationTime).minusMinutes(10))).findFirst().get();
	}

}
