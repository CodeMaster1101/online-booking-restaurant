package com.mile.pc.mile.restoraunt.app.service;

import java.time.LocalTime;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mile.pc.mile.restoraunt.app.comparator.TimeComparator;
import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;
import com.mile.pc.mile.restoraunt.app.dao.UserPasswordForm;
import com.mile.pc.mile.restoraunt.app.model.CustomTable;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

import lombok.SneakyThrows;

@Service @Transactional
public class MainService {

	@Autowired CustomTableRepository tRepo;
	@Autowired UserRepository uRepo;
	@Autowired ReservationRepository rRepo;

	@SneakyThrows
	public void reserveTable(Reservation reservation) {
		if(!checkTime(reservation))
			throw new Exception("can't reserve a table after the current time of the day");
		User user = uRepo.findByUsername(reservation.getUser().getUsername());
		reservation.setUser(user);
		reservation.setTable(tRepo.findById(reservation.getTable().getId()).get());
		if(!reservationRequirements(reservation))
			throw new Exception("didnt meet the reservation requierements");
		checkOtherReservations(reservation.getTable(), reservation);
		user.setReservation(reservation);
	}
	
	@SneakyThrows
	public void cancelReservation(UserPasswordForm user) {
		User localUser = uRepo.findByUsername(user.getUsername());
		if(!cancelReservationRequirements(localUser, localUser.getReservation()))
			throw new Exception("didnt meet the canceling requierements");
		Reservation reservation = localUser.getReservation();
		reservation.getTable().removeReservation(reservation);
		localUser.setReservation(null);
		reservation.setUser(null);
	}

	/*
	 * PRIVATE HELPING METHODS
	 */

	@SneakyThrows
	private void checkOtherReservations(CustomTable table, Reservation reservation) {	
		if(table.getReservations().isEmpty()) {
			table.addReservation(reservation);
			return;
		}
		table.getReservations().add(reservation);
		TimeComparator tComp = new TimeComparator();
		Collections.sort(table.getReservations(), tComp);
		if(tComp.isGoodDistance() == false) {
			table.getReservations().remove(reservation);
			throw new Exception("distance between reservations is bad");
		}
	}

	
	private boolean checkTime(Reservation res) {
//		if(CONSTANTS.parseLocalTime(res.getTime()).isBefore(LocalTime.now()))
//			return false;
		return true;
	}
	private boolean reservationRequirements(Reservation reservation) {

		if(reservation.getUser().getBalance() < CONSTANTS.fee)
			return false;
		
		if(reservation.getAccepted() == false)
			return false;

//		LocalTime time = CONSTANTS.parseLocalTime(reservation.getTime());
//		if(!(time.isAfter(LocalTime.parse("07:30")) && time.isBefore(LocalTime.parse("22:00") )&& time.isAfter(LocalTime.now().plusMinutes(15))))
//			return false;
		return true;
	}

	private Boolean cancelReservationRequirements(User user, Reservation reservation) {
		if(user.getUsername() != reservation.getUser().getUsername() || user.getPassword() != reservation.getUser().getPassword())
			return false;
		LocalTime currentTime = LocalTime.now();
		//if reservation time <= currentTime < reservation.time + 35 minutes.
		if(!(currentTime.isBefore(CONSTANTS.parseLocalTime(reservation.getTime()).plusMinutes(35))))
			return false;
		return true;
	}

}

