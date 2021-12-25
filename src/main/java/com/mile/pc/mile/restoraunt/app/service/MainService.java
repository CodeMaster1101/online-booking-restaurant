package com.mile.pc.mile.restoraunt.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mile.pc.mile.restoraunt.app.comparator.TimeComparator;
import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;
import com.mile.pc.mile.restoraunt.app.dao.UserPasswordForm;
import com.mile.pc.mile.restoraunt.app.model.CustomTable;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.TimeContainer;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.TimeContainerRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@Service @Transactional @NoArgsConstructor
public class MainService {

	@Autowired TimeContainerRepository reservationTimeRepo;
	@Autowired CustomTableRepository tRepo;
	@Autowired UserRepository uRepo;
	@Autowired ReservationRepository rRepo;

	@SneakyThrows
	public void reserveTable(Reservation reservation) {
		if(!checkTime(reservation))
			throw new Exception("can't reserve a table after the current time of the day");
		User user = uRepo.findByUsername(reservation.getUser().getUsername());
		if(user.getReservation() != null)
			throw new Exception("already a reservation on this user");
		user.setUReservation(reservation);
		reservation.setTable(tRepo.findById(reservation.getTable().getId()).get());	
		if(!reservationRequirements(reservation))
			throw new Exception("didnt meet the reservation requierements");
		checkOtherReservations(reservation.getTable(), reservation);
		reservationTimeRepo.save(new TimeContainer(null, LocalDateTime.now(), user));
		user.setBalance(user.getBalance() - CONSTANTS.fee);
		if(tableFull(reservation.getTable()))
			reservation.getTable().setFull(true);			
	}
	@SneakyThrows
	public void cancelReservation(UserPasswordForm user) {
		User localUser = uRepo.findByUsername(user.getUsername());
		if(!cancelReservationRequirements(localUser, localUser.getReservation()))
			throw new Exception("didnt meet the canceling requierements");
		basicUserReservationCancelLogic(localUser);
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
		table.addReservation(reservation);
		TimeComparator tComp = new TimeComparator();
		Collections.sort(table.getReservations(), tComp);
		if(tComp.isGoodDistance() == false) {
			table.removeReservation(reservation);
			throw new Exception("distance between reservations is bad");
		}
	}
	private boolean tableFull(CustomTable table) {
		LocalTime lastReservation = OffsetDateTime.parse(CONSTANTS.END.toString()).toLocalTime();
		return table.getReservations().stream()
				.anyMatch(r ->OffsetDateTime.parse(r.getTime().toString()).toLocalTime().isAfter(lastReservation.minusHours(3).minusMinutes(1))
						|| OffsetDateTime.parse(r.getTime().toString()).toLocalTime().equals(lastReservation));		   
	}
	private boolean checkTime(Reservation res) {
				if(res.getTime().isBefore(LocalDateTime.now()))
					return false;
		return true;
	}
	private boolean reservationRequirements(Reservation reservation) {
		if(reservation.getUser().getBalance() < CONSTANTS.fee)
			return false;
		if(reservation.getAccepted() == false)
			return false;
				LocalDateTime now = LocalDateTime.now();
				if(!(now.isAfter(LocalDateTime.of(LocalDate.now(), CONSTANTS.START)) && now.isBefore(LocalDateTime.of(LocalDate.now(), CONSTANTS.END))))
					return false;
		return true;
	}
	private Boolean cancelReservationRequirements(User user, Reservation reservation) {
		if(user.getUsername() != reservation.getUser().getUsername() || user.getPassword() != reservation.getUser().getPassword())
			return false;
		TimeContainer reservationMoment = reservationTimeRepo.findByUser(user);
		if(!(reservationMoment.getTime().isBefore(LocalDateTime.now().plusMinutes(15))))
			return false;
		return true;
	}
	private void basicUserReservationCancelLogic(User localUser) {
		localUser.getReservation().getTable().removeReservation(localUser.getReservation());
		localUser.setReservation(null);
		reservationTimeRepo.deleteById(localUser.getTimeContainer().getId());
	}

}
