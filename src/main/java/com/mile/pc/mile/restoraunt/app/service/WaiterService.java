package com.mile.pc.mile.restoraunt.app.service;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;
import com.mile.pc.mile.restoraunt.app.model.CustomTable;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

import lombok.SneakyThrows;

@Service @Transactional
public class WaiterService {

	@Autowired CustomTableRepository tRepo;
	@Autowired ReservationRepository reservations;
	@Autowired UserRepository urepo;

	public void setBusy(long tableid) {
		CustomTable table = tRepo.findById(tableid).get();
		if(table.getBusy()!=true) {
			userArrived(findCurrentReservation(table).getId());
			table.setBusy(true);
		}
	}
	private Reservation findCurrentReservation(CustomTable table) {
		//find current time -> predicate(res.isAfter())
		return null;
	}
	public void setCalm(long tableid) {
		CustomTable table = tRepo.findById(tableid).get();
			table.setBusy(false);
		}
	//PRIVATE HELPING METHODS
	private boolean checkUserOnTime(long reservationId) {
		Reservation reservation = reservations.findById(reservationId).get();
		LocalTime now = LocalTime.now();
		User user = reservation.getUser();
		if(!(now.isAfter(CONSTANTS.parseLocalTime(user.getReservation().getTime())) &&
				now.isBefore(CONSTANTS.parseLocalTime(user.getReservation().getTime()).plusMinutes(40)))) {
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
	
}
