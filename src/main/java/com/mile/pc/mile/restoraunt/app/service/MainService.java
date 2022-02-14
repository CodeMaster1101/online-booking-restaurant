package com.mile.pc.mile.restoraunt.app.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mile.pc.mile.restoraunt.app.comparator.TimeComparator;
import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;
import com.mile.pc.mile.restoraunt.app.dto.ReservationDTO;
import com.mile.pc.mile.restoraunt.app.dto.UserPasswordForm;
import com.mile.pc.mile.restoraunt.app.model.CustomTable;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

import lombok.SneakyThrows;


@Service  
public class MainService {

	@Autowired ReservationRepository reservations;
	@Autowired CustomTableRepository tRepo;
	@Autowired UserRepository uRepo;
	@Autowired ReservationRepository rRepo;
	@Autowired WaiterService w_service;


	@SneakyThrows @Transactional
	public void reserveTable(ReservationDTO dto) {		
		Reservation reservation = saveNewRes(dto);
		if(!reservationRequirements(reservation, dto))
			throw new Exception("didn't meet reservation requirements");
		checkOtherReservations(reservation.getTable(), reservation);
		User user = reservation.getUser();
		basicReservingProcedure(user, reservation);
	}

	@SneakyThrows @Transactional
	public boolean cancelReservation(UserPasswordForm dto) {
		User localUser = uRepo.findByUsername(dto.getUsername());
		if(!passwordWithUser(localUser, dto.getPassword()))
			throw new Exception("didnt meet the canceling requierements");
		if(refund(localUser)) {
			localUser.setBalance(localUser.getBalance() + localUser.getReservation().getFee());
			basicUserReservationCancelLogic(localUser);
			return true;
		}return false;
	}

	/*
	 * PROTECTED METHODS
	 */


	@SneakyThrows @Transactional
	protected void checkOtherReservations(CustomTable table, Reservation reservation) {	
		table.getReservations().add(reservation);
		TimeComparator tComp = new TimeComparator();
		Collections.sort(table.getReservations(), tComp);
		if(tComp.isGoodDistance() == false) {
			table.removeReservation(reservation);
			throw new Exception("distance between reservations is bad");
		}
	}

	/*
	 * PRIVATE HELPING METHODS
	 */
	@SneakyThrows
	private boolean reservationRequirements(Reservation reservation, ReservationDTO dto) {
		if(reservation.getUser().getBalance() < incrementFee(reservation.getPeriod()))
			return false;
		if(reservation.getAccepted() == false && reservation.getUser().getReservation() != null)
			return false;
		if(!OneDayBefore(reservation))
			throw new Exception("one day minimum for reserving ahead");
		if(!passwordWithUser(reservation.getUser(), dto.getPassword()))
			throw new Exception("wrong password");
		if(!specificTimeBasedOnPeriod(dto))
			throw new Exception("specific time out of range");
		return true;
	}

	private boolean specificTimeBasedOnPeriod(ReservationDTO dto) {
		if(dto.getPeriod() == 1) {
			if(dto.getTime().isAfter(CONSTANTS.START.minusMinutes(1)) && 
					dto.getTime().isBefore(CONSTANTS.NOON))return true;
		}
		else if(dto.getPeriod() == 2) {
			if(dto.getTime().isAfter(CONSTANTS.NOON) && 
					dto.getTime().isBefore(CONSTANTS.EVENING))return true;
		}
		else if(dto.getPeriod() == 3) {
			if(dto.getTime().isAfter(CONSTANTS.EVENING) && 
					dto.getTime().isBefore(CONSTANTS.END.plusMinutes(1)))return true;
		}
		return false;
	}

	private boolean passwordWithUser(User user, String password) {
		if(user.getPassword().contentEquals(password))
			return true;
		return false;
	}

	private boolean refund(User user) {
		if(!(LocalDateTime.now().isBefore(user.getReservationMoment().plusMinutes(10))))
			return false;
		return true;
	}

	@Transactional
	private void basicUserReservationCancelLogic(User localUser) {
		Reservation res = localUser.getReservation();
		localUser.setReservation(null);
		res.getTable().removeReservation(res);
		reservations.deleteById(res.getId());
		localUser.setReservationMoment(null);
	}
	
	private void basicReservingProcedure(User user, Reservation reservation) {
		user.setReservation(reservation);
		user.setReservationMoment(LocalDateTime.now());
		reservation.setFee(incrementFee(reservation.getPeriod()));
		user.setBalance(user.getBalance() - reservation.getFee());		
	}
	
	private boolean OneDayBefore(Reservation reservation) {
		if(LocalDateTime.now().plusDays(1).isBefore(reservation.getTime()))
			return true;
		return false;
	}

	@SneakyThrows
	private long incrementFee(int period) {
		switch (period) {
		case 1: return CONSTANTS.BREAKFAST_FEE;
		case 2: return CONSTANTS.LUNCH_FEE;
		case 3: return CONSTANTS.DINNER_FEE;
		default: throw new Exception("no fee 1-3");  
		}
	}

	private Reservation saveNewRes(ReservationDTO dto) {
		return reservations.save(new Reservation(null, dto.isAccepted(), uRepo.findByUsername(dto.getUsername())
				,findAvailableTable(dto), LocalDateTime.of(dto.getDate(), dto.getTime()), null, false, false, dto.getPeriod(), dto.getNote()));
	}

	@SneakyThrows
	private CustomTable findAvailableTable(ReservationDTO dto) {
		for (CustomTable table : tRepo.findAll()) {
			Optional<Reservation> res = table.getReservations().stream().filter(r -> r.getPeriod() == dto.getPeriod()).findFirst();
			if(!res.isPresent())
				return table;
		}
		throw new Exception("no available tables for this period of the day.");
	}
}
