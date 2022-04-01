package com.mile.pc.mile.restoraunt.app.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;
import com.mile.pc.mile.restoraunt.app.dto_dao.ReservationDTO;
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

/**
 * 	The Public section is based on the client's functionalities. Every client has the following functionalities:
 * 
	- Reserving a table at a certain time and day.
	- Canceling the same reservation

 * @author Mile Stanislavov
 *
 */
@Service  
public class MainService {

	@Autowired ReservationRepository reservations;
	@Autowired TableRepository tRepo;
	@Autowired UserRepository uRepo;
	@Autowired ReservationRepository rRepo;
	@Autowired WaiterService w_service;

	/**
	  The client must fill in a form containing the username and password of the client. 
	  The form allows the user to select the date for the reservation using a calendar. 
	  Now, for the service to be functional and fair for every client, 
	  the form asks the client at which period of the day they would like to attend to their reservation. There are three periods,
	  "Breakfast" {07:00 AM - 12:00 PM}, "Lunch" {01:00 PM - 07:00 PM}, "Dinner" {08:00 PM - 10:00 PM}. 
	  Following the period, the user needs to specify a time of arrival within their respective period interval. 
	  Every reservation has a "fee" that the client must pay in advance to insure that the they will in fact show up on the reservation. 
	  Breakfast - 150. Lunch - 300. Dinner - 600.
	  This must be accepted. The minimal time to reserve must be within a day from now. 
	  For example, if today is 18/2/22, the minimum time to make a reservation would be on 19/2/22. 

	 * @param dto - the data transfer object corresponding to the reservation form that the user fills in.
	 */
	@SneakyThrows @Transactional
	public void reserveTable(ReservationDTO dto) {		
		Reservation reservation = saveNewRes(dto);
		reservationRequirements(reservation, dto);
		reservation.setFee(getPeriodInFee(reservation.getPeriod()));
		User user = reservation.getUser();
		basicReservingProcedure(user, reservation);
	}
	/**
	  The canceling procedure must be executed within 2 hours from the reserving moment. For example if the current time is 06:00 PM
	  the client must cancel their reservation until 08:00 PM. If the client doesn't cancel it within two hours, the reservation lives on.
	  @param username - the logged in user
	  @throws TimeOutForCancelingException - custom exception
	 */
	@Transactional
	public void cancelReservation(String username) throws TimeOutForCancelingException {
		User localUser = uRepo.getUserByUsername(username);
		if(refund(localUser)) {
			localUser.setBalance(localUser.getBalance() + localUser.getReservation().getFee());
			basicUserReservationCancelLogic(localUser);
		} else
			throw new TimeOutForCancelingException();
	}

	
	// private/protected methods
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
		LocalTime time = dto.getTime();		
		if(dto.getPeriod() == 1) {
			if(time.isAfter(CONSTANTS.START.minusMinutes(1)) && 
					time.isBefore(CONSTANTS.NOON.plusMinutes(1)))return true;
		}
		else if(dto.getPeriod() == 2) {
			if(time.isAfter(CONSTANTS.NOON.plusMinutes(59)) && 
					time.isBefore(CONSTANTS.EVENING.minusMinutes(59)))return true;
		}
		else if(dto.getPeriod() == 3) {
			if(time.isAfter(CONSTANTS.EVENING.minusMinutes(1)) && 
					time.isBefore(CONSTANTS.END.plusMinutes(1)))return true;
		}
		return false;
	}

	private boolean refund(User user) {
		if(!(LocalDateTime.now().isBefore(user.getReservationMoment().plusHours(CONSTANTS.CANCEL_TIME).plusMinutes(1))))
			return false;
		return true;
	}

	private void basicUserReservationCancelLogic(User localUser) {
		Reservation res = localUser.getReservation();
		localUser.setReservation(null);
		res.getTable().removeReservation(res);
		localUser.setReservationMoment(null);
	}

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
