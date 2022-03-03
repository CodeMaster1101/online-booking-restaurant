package com.mile.pc.mile.restoraunt.app.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;
import com.mile.pc.mile.restoraunt.app.dto.ReservationDTO;
import com.mile.pc.mile.restoraunt.app.exceptions.BadReservationRadiusException;
import com.mile.pc.mile.restoraunt.app.exceptions.InvalidSpecificTimeException;
import com.mile.pc.mile.restoraunt.app.exceptions.NoAvailableTablesTodayException;
import com.mile.pc.mile.restoraunt.app.exceptions.NotMetReservingRequirementsException;
import com.mile.pc.mile.restoraunt.app.exceptions.TimeOutForCancelingException;
import com.mile.pc.mile.restoraunt.app.exceptions.invalidPeriodException;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.RestorauntTable;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.TableRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

import lombok.SneakyThrows;


@Service  
public class MainService {

	@Autowired ReservationRepository reservations;
	@Autowired TableRepository tRepo;
	@Autowired UserRepository uRepo;
	@Autowired ReservationRepository rRepo;
	@Autowired WaiterService w_service;

	@SneakyThrows @Transactional
	public void reserveTable(ReservationDTO dto) {		
		Reservation reservation = saveNewRes(dto);
		reservationRequirements(reservation, dto);
		reservation.setFee(getPeriodInFee(reservation.getPeriod()));
		User user = reservation.getUser();
		basicReservingProcedure(user, reservation);
	}

	@Transactional 
	public void cancelReservation(String username) throws TimeOutForCancelingException {
		User localUser = uRepo.getUserByUsername(username);
		if(refund(localUser)) {
			localUser.setBalance(localUser.getBalance() + localUser.getReservation().getFee());
			basicUserReservationCancelLogic(localUser);
		}else {
			throw new TimeOutForCancelingException();
		}
	}

	/*
	 * PRIVATE HELPING METHODS
	 */
	@SneakyThrows
	private void reservationRequirements(Reservation reservation, ReservationDTO dto) {
		if(reservation.getUser().getBalance() < getPeriodInFee(reservation.getPeriod()))
			throw new NotMetReservingRequirementsException();
		if(!OneDayBefore(reservation))
			throw new BadReservationRadiusException(reservation.getTime());
		if(!specificTimeBasedOnPeriod(dto))
			throw new InvalidSpecificTimeException(dto.getTime(), dto.getPeriod());
	}

	private boolean specificTimeBasedOnPeriod(ReservationDTO dto) {
		if(dto.getPeriod() == 1) {
			if(dto.getTime().isAfter(CONSTANTS.START.minusMinutes(1)) && 
					dto.getTime().isBefore(CONSTANTS.NOON.plusMinutes(30)))return true;
		}
		else if(dto.getPeriod() == 2) {
			if(dto.getTime().isAfter(CONSTANTS.NOON.plusMinutes(59)) && 
					dto.getTime().isBefore(CONSTANTS.EVENING.minusHours(1)))return true;
		}
		else if(dto.getPeriod() == 3) {
			if(dto.getTime().isAfter(CONSTANTS.EVENING.minusMinutes(1)) && 
					dto.getTime().isBefore(CONSTANTS.END.plusMinutes(1)))return true;
		}
		return false;
	}

	private boolean refund(User user) {
		if(!(LocalDateTime.now().isBefore(user.getReservationMoment().plusHours(CONSTANTS.CANCEL_TIME))))
			return false;
		return true;
	}

	@Transactional
	private void basicUserReservationCancelLogic(User localUser) {
		Reservation res = localUser.getReservation();
		localUser.setReservation(null);
		res.getTable().removeReservation(res);
		localUser.setReservationMoment(null);
	}

	@Transactional
	private void basicReservingProcedure(User user, Reservation reservation) throws Exception {
		reservation.getTable().addReservation(reservation);
		user.setReservation(reservation);
		user.setReservationMoment(LocalDateTime.now());
		user.setBalance(user.getBalance() - reservation.getFee());		
	}

	private boolean OneDayBefore(Reservation reservation) {
		//		if(LocalDateTime.now().plusDays(CONSTANTS.RADIUS).isBefore(reservation.getTime()))
		return true;
		//return false;
	}

	@SneakyThrows
	private long getPeriodInFee(int period) {
		switch (period) {
		case 1: return CONSTANTS.BREAKFAST_FEE;
		case 2: return CONSTANTS.LUNCH_FEE;
		case 3: return CONSTANTS.DINNER_FEE;
		default: throw new invalidPeriodException(); 
		}
	}

	@Transactional @SneakyThrows
	private Reservation saveNewRes(ReservationDTO dto) {
		User user = uRepo.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if(user.getReservation() != null) throw new NotMetReservingRequirementsException();
		return reservations.save(new Reservation(null, dto.isAccepted(), user,findAvailableTable(dto), LocalDateTime.of(dto.getDate(), dto.getTime()), null, false, dto.getPeriod(), dto.getNote()));
	}

	@SneakyThrows
	private RestorauntTable findAvailableTable(ReservationDTO dto) {
		for (RestorauntTable table : tRepo.findAll()) {
			Optional<Reservation> res = table.getReservations().stream().filter(r -> r.getPeriod() == dto.getPeriod() && (r.getTime().toLocalDate().isEqual(dto.getDate()))).findFirst();
			if(!res.isPresent())
				return table;
		}
		throw new NoAvailableTablesTodayException(dto.getDate());
	}
	
}
