package com.mile.pc.mile.restoraunt.app.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mile.pc.mile.restoraunt.app.comparator.TimeComparator;
import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;
import com.mile.pc.mile.restoraunt.app.dto.publi.ReservationDTO;
import com.mile.pc.mile.restoraunt.app.dto.publi.UserPasswordForm;
import com.mile.pc.mile.restoraunt.app.model.CustomTable;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

import lombok.SneakyThrows;

/**
 * Service based on the Client's functionalities
 * It allows the user to reserve a table on his or her name on a certain table in a specific time
 * The User can also cancel their reservation, ofCourse, in the given time.
 * 
 * @author Mile Stanislavov
 * 
 */
@Service  
public class MainService {

	@Autowired ReservationRepository reservations;
	@Autowired CustomTableRepository tRepo;
	@Autowired UserRepository uRepo;
	@Autowired ReservationRepository rRepo;
	@Autowired WaiterService w_service;
	
	/**
	 * Makes a reservation on a client's name on a certain table at a specific date and time.
	 * Checks if all the reservations are distanced correctly, if the time of the reservation is
	 * in a compatible time. Does a few more tests, such as checking if the user has enough money, if the table is full, etc...
	 * Removes money from the user based on how much hours the reservation contains.
	 * The removed money from the user is being kept on the reservation under the field "fee".
	 * Finally if there weren't any exceptions during the process, all changes are flushed into the DB. 
	 * @param reservation -> the object that contains all the startup information
	 */
	@SneakyThrows @Transactional
	public void reserveTable(ReservationDTO dto) {
		Reservation reservation = reservations.save(new Reservation(null, dto.isAccepted(), uRepo.findByUsername(dto.getUsername()), 
				tRepo.findById(dto.getTableid()).get(), dto.getTime(), LocalDateTime.of(dto.getTime().toLocalDate(), dto.getMaxTime()), 0, null, null, null));
		if(checkReservationRadius(reservation) == false)
			throw new Exception("bad radius");
		if(!checkTime(reservation))
			throw new Exception("can't reserve a table after the current time of the day");
		reservation.getUser().setUsername(dto.getUsername());
		String password = dto.getPassword();
		User user = reservation.getUser();
		if(password.equals(user.getPassword()) == false)
			throw new Exception("password incorrect");
		if(user.getReservation() != null)
			throw new Exception("already a reservation on this user");
		user.setUReservation(reservation);
		reservation.setUTable(tRepo.findById(reservation.getTable().getId()).get());	
		if(!reservationRequirements(reservation))
			throw new Exception("didnt meet the reservation requierements");
		checkOtherReservations(reservation.getTable(), reservation);
		user.setReservationMoment(LocalDateTime.now());
		reservation.setFee(CONSTANTS.FEE * incrementFee(reservation.getUser()));
		user.setBalance(user.getBalance() - reservation.getFee());			
	}
	
	/**
	 * Cancels the reservation if and only if the user has canceled it in the given time. 
	 * Otherwise, the money will not be returned to the user, but the reservation will stay in case
	 * the user decides to come and not let the money be in vein.
	 * @param user
	 * @return true if all the conditions are met
	 */
	@SneakyThrows @Transactional
	public boolean cancelReservation(UserPasswordForm user) {
		User localUser = uRepo.findByUsername(user.getUsername());
		if(!cancelReservationRequirements(localUser, user.getPassword()))
			throw new Exception("didnt meet the canceling requierements");
		if(refund(localUser, localUser.getReservation())) {
			localUser.setBalance(localUser.getBalance() + localUser.getReservation().getFee());
			basicUserReservationCancelLogic(localUser);
			return true;
		}return false;
	}
	
	/*
	 * PROTECTED METHODS
	 */
	
	/**
	 * Checks if the time intervals for each reservation is not overlapping another.
	 * if and only if there aren't any reservation on the table, the reservation will be added
	 * without doing any comparing between two reservations.
	 * @throws custom exception("distance is bad"), if one reservation overlaps another
	 * @param table the object to which the reservation will be added
	 * @param reservation the object to be added in a collection on the table.
	 */
	@SneakyThrows
	protected void checkOtherReservations(CustomTable table, Reservation reservation) {	
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
	
	/**
	 * Checks if the user wants to make a reservation before the current time,
	 * which would be impossible
	 * @param res
	 * @return true if the current time is after the reservation time
	 */
	private boolean checkTime(Reservation res) {
		if(res.getTime().isBefore(LocalDateTime.now()))
			return false;
		return true;
	}
	
	/**
	 * This method checks the following conditions: 
	 * - the user must have more money than their fee.
	 * - reservation must be accepted
	 * - the maximum stay must be before 23:59 -> MAX_TIME
	 * - reservation time must be after LocalDateTime(reservation date, 06:59)
	 * - the end of the reservation must be before LocalDateTime(end of reservation date, MAX_TIME -> 23:59)
	 * @param reservation
	 * @return true if every condition is met
	 */
	private boolean reservationRequirements(Reservation reservation) {
		if(reservation.getUser().getBalance() < reservation.getUser().getBalance() - 
				CONSTANTS.FEE * incrementFee(reservation.getUser())) {
			return false;
		}	
		if(reservation.getAccepted() == false)
			return false;
		if(!(reservation.getTime().isAfter(LocalDateTime.of(reservation.getTime().toLocalDate(), CONSTANTS.START.minusMinutes(1)))&&
				reservation.getMaxTime().isBefore(LocalDateTime.of(reservation.getMaxTime().toLocalDate(), CONSTANTS.MAX_TIME))))
			return false;
		else 
			return true;
	}
	
	/**
	 * Since the reservation moment of the user, if 16 minutes have passed, 
	 * then they wont be having a refund. 
	 * @param user 
	 * @param reservation
	 * @return true if 16 minutes haven't passed
	 */
	private boolean refund(User user, Reservation reservation) {
		if(!(LocalDateTime.now().isBefore(user.getReservationMoment().plusMinutes(16))))
			return false;
		return true;
	}
	
	/**
	 * If the username does not match the password of the user, then he wont be able to cancel it,
	 * as either he has forgotten it, or its not the same user that has reserved the table in the first place.
	 * @param user
	 * @param reservation
	 * @return true if the username matches the password
	 */
	private boolean cancelReservationRequirements(User user, String password) {

		if(user.getPassword().contentEquals(password))
			return true;
		return false;
	}
	
	/**
	 * Removes the reservation from the DB via ("orphan removal"), referenced by the localUser and the table reference. 
	 * In other words, the reservation is being cut off from every dependent reference. Sets the reservationMoment to null, 
	 * as the reservation has been canceled. 
	 * @param localUser
	 */
	@Transactional
	private void basicUserReservationCancelLogic(User localUser) {
		Reservation res = localUser.getReservation();
		localUser.setReservation(null);
		res.getTable().removeReservation(res);
		reservations.deleteById(res.getId());
		localUser.setReservationMoment(null);
	}
	
	/**
	 * Checks if the end of the reservation is not before the start of the reservation.
	 * Also if they differ by 8 or more, then the reservation time would be too long and it will break.
	 * On the other hand it would brake as well if there isn't at least one hour difference between the end
	 * and the start of the reservation.
	 * the transaction.
	 * @param reservation
	 * @return true if every condition is met
	 */
	private boolean checkReservationRadius(Reservation reservation) {
		if(reservation.getMaxTime().isBefore(reservation.getTime()))
			return false;	
		if(Duration.between(reservation.getTime(), reservation.getMaxTime()).toHours() > 8l 
				&& !(reservation.getTime().isBefore(reservation.getMaxTime().minusHours(1).plusMinutes(1))))
			return false;
		return true;
	}
	
	/** 
	 * Counts the duration between the end of the reservation and the beginning, 
	 * then that duration is used to find how much times the CONSTANS.FEE value will be multiplied by.
	 * @param user, the reservation on this user object that will be tested
	 * @return the duration as hours in long format.
	 */
	private long incrementFee(User user) {
		return Duration.between(user.getReservation().getTime(), user.getReservation().getMaxTime()).toHours();
	}

}
