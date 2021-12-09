package com.mile.pc.mile.restoraunt.app.service;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		CustomTable table = tRepo.findById(reservation.getTable().getId()).get();
		User user = uRepo.findByUsername(reservation.getUser().getUsername());
		Reservation myReservation = new Reservation(null, reservation.getAccepted(), user, table, reservation.getTime());
		rRepo.saveAndFlush(myReservation);
		if(!(reservationRequirements(rRepo.findByUser(user))))
			throw new Exception("something went wrong with the reservation, regarding the requierements");
		else {
			user.setBalance(user.getBalance() - CONSTANTS.fee );
			user.setReservation(myReservation);
			table.setReservation(myReservation);
			table.setAvailable(false);
			uRepo.saveAndFlush(user);
			tRepo.saveAndFlush(table);
			rRepo.save(myReservation);
		}
	}

	@SneakyThrows
	public void cancelReservation(UserPasswordForm user) {
		User myUser = uRepo.findByUsername(user.getUsername());
		Reservation reservation = rRepo.findByUser(myUser);
		if(!(cancelReservationRequirements(myUser, reservation)))
			throw new Exception("the time has passed for for canceling or its not the owner of the reservation");
		else {
			CustomTable table = tRepo.findById(reservation.getTable().getId()).get();
			table.setArrived(false);
			table.setAvailable(true);
			table.setReservation(null);
			myUser.setBalance(myUser.getBalance() + CONSTANTS.fee);
			myUser.setReservation(null);
			uRepo.saveAndFlush(myUser);
			tRepo.saveAndFlush(table);
		}
	}
	/*
	 * PRIVATE HELPING METHODS
	 */
	private Boolean reservationRequirements(Reservation reservation) {

		if(reservation.getUser().getBalance() < CONSTANTS.fee)
			return false;

		if(reservation.getAccepted() == false)
			return false;
		//if the table is occupied
		if(reservation.getTable().getAvailable() == null) {

		}else {
			if(reservation.getTable().getAvailable() == false)
				return false;
		}
		LocalTime time = CONSTANTS.parseLocalTime(reservation.getTime());
		if(!(time.isAfter(LocalTime.parse("07:30")) && time.isBefore(LocalTime.parse("22:00") )&& time.isAfter(LocalTime.now().plusMinutes(15))))
			return false;
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
