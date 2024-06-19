package com.mile.pc.mile.restoraunt.app.service;

import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;
import com.mile.pc.mile.restoraunt.app.dto_dao.ReservationDTO;
import com.mile.pc.mile.restoraunt.app.exceptions.*;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.RestaurantTable;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.TableRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

/**
 * The MainService class provides functionalities for clients of the restaurant app.
 * Clients can perform the following actions:
 * <p>
 * - Reserving a table at a certain time and day.
 * - Canceling their reservation.
 * <p>
 * The reservation system includes specific periods for different times of the day
 * and ensures fairness by implementing a fee structure and cancellation rules.
 *
 * @see User
 * @see Reservation
 * @see RestaurantTable
 * @see ReservationRepository
 * @see TableRepository
 * @see UserRepository
 *
 * @autor Mile Stanislavov
 */
@Service
public class MainService {

	private final TableRepository tableRepository;
	private final UserRepository userRepository;
	private final ReservationRepository reservationRepository;

	/**
	 * Constructor to initialize repositories for table, user, and reservation entities.
	 *
	 * @param tableRepository the repository to manage RestaurantTable entities
	 * @param userRepository the repository to manage User entities
	 * @param reservationRepository the repository to manage Reservation entities
	 */
	public MainService(TableRepository tableRepository, UserRepository userRepository, ReservationRepository reservationRepository) {
		this.tableRepository = tableRepository;
		this.userRepository = userRepository;
		this.reservationRepository = reservationRepository;
	}

	/**
	 * Allows a client to reserve a table based on the provided reservation details.
	 * The client must specify a period and time of arrival, and pay a fee in advance.
	 *
	 * @param dto the data transfer object corresponding to the reservation form that the user fills in.
	 * @throws NotMetReservingRequirementsException if the client's balance is insufficient
	 * @throws BadReservationRadiusException if the reservation is not made at least one day in advance
	 * @throws InvalidSpecificTimeException if the specified time does not match the selected period
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
	 * Allows a client to cancel their reservation within two hours of making it.
	 *
	 * @param username the logged-in user's username
	 * @throws TimeOutForCancelingException if the cancellation period has expired
	 */
	@Transactional
	public void cancelReservation(String username) throws TimeOutForCancelingException {
		User localUser = userRepository.findByUsername(username);
		if (refund(localUser)) {
			localUser.setBalance(localUser.getBalance() + localUser.getReservation().getFee());
			basicUserReservationCancelLogic(localUser);
		} else {
			throw new TimeOutForCancelingException();
		}
	}

	/**
	 * Validates the reservation requirements, including user's balance, reservation timing, and period.
	 *
	 * @param reservation the reservation entity
	 * @param dto the reservation data transfer object
	 * @throws NotMetReservingRequirementsException if the client's balance is insufficient
	 * @throws BadReservationRadiusException if the reservation is not made at least one day in advance
	 * @throws InvalidSpecificTimeException if the specified time does not match the selected period
	 */
	@SneakyThrows
	private void reservationRequirements(Reservation reservation, ReservationDTO dto) {
		if (reservation.getUser().getBalance() < getPeriodInFee(reservation.getPeriod()))
			throw new NotMetReservingRequirementsException();
		if (!oneDayBefore(reservation))
			throw new BadReservationRadiusException(reservation.getTime());
		if (!specificTimeBasedOnPeriod(dto))
			throw new InvalidSpecificTimeException(dto.getPeriod());
	}

	/**
	 * Checks if the specified time is valid for the selected period.
	 *
	 * @param dto the reservation data transfer object
	 * @return true if the time is valid for the period, false otherwise
	 */
	private boolean specificTimeBasedOnPeriod(ReservationDTO dto) {
		LocalTime time = dto.getTime();
		if (dto.getPeriod() == 1) {
			return time.isAfter(CONSTANTS.START.minusMinutes(1)) && time.isBefore(CONSTANTS.NOON.plusMinutes(1));
		} else if (dto.getPeriod() == 2) {
			return time.isAfter(CONSTANTS.NOON.plusMinutes(59)) && time.isBefore(CONSTANTS.EVENING.minusMinutes(59));
		} else if (dto.getPeriod() == 3) {
			return time.isAfter(CONSTANTS.EVENING.minusMinutes(1)) && time.isBefore(CONSTANTS.END.plusMinutes(1));
		}
		return false;
	}

	/**
	 * Checks if a refund can be issued based on the cancellation time frame.
	 *
	 * @param user the user entity
	 * @return true if the refund is allowed, false otherwise
	 */
	private boolean refund(User user) {
		return LocalDateTime.now().isBefore(user.getReservationMoment().plusHours(CONSTANTS.CANCEL_TIME).plusMinutes(1));
	}

	/**
	 * Executes the basic logic for canceling a user's reservation.
	 *
	 * @param localUser the user entity whose reservation is being canceled
	 */
	private void basicUserReservationCancelLogic(User localUser) {
		Reservation res = localUser.getReservation();
		localUser.setReservation(null);
		res.getTable().removeReservation(res);
		localUser.setReservationMoment(null);
	}

	/**
	 * Executes the basic procedure for reserving a table.
	 *
	 * @param user the user entity making the reservation
	 * @param reservation the reservation entity
	 */
	private void basicReservingProcedure(User user, Reservation reservation) {
		reservation.getTable().addReservation(reservation);
		user.setReservation(reservation);
		user.setReservationMoment(LocalDateTime.now());
		user.setBalance(user.getBalance() - reservation.getFee());
	}

	/**
	 * Checks if the reservation is made at least one day in advance.
	 *
	 * @param reservation the reservation entity
	 * @return true if the reservation is made at least one day in advance, false otherwise
	 */
	private boolean oneDayBefore(Reservation reservation) {
		return LocalDateTime.now().plusDays(CONSTANTS.MINIMUM_DAYS).isBefore(reservation.getTime());
	}

	/**
	 * Retrieves the fee for a given reservation period.
	 *
	 * @param period the reservation period
	 * @return the fee corresponding to the period
	 * @throws InvalidPeriodException if the period is invalid
	 */
	@SneakyThrows
	private long getPeriodInFee(int period) {
		switch (period) {
		case 1: return CONSTANTS.BREAKFAST_FEE;
		case 2: return CONSTANTS.LUNCH_FEE;
		case 3: return CONSTANTS.DINNER_FEE;
		default: throw new InvalidPeriodException();
		}
	}

	/**
	 * Saves a new reservation based on the provided DTO.
	 *
	 * @param dto the reservation data transfer object
	 * @return the saved reservation entity
	 * @throws NotMetReservingRequirementsException if the user already has a reservation
	 */
	@Transactional @SneakyThrows
	public Reservation saveNewRes(ReservationDTO dto) {
		User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if (user.getReservation() != null) throw new NotMetReservingRequirementsException();
		return reservationRepository.save(new Reservation(null, dto.isAccepted(), user, findAvailableTable(dto), LocalDateTime.of(dto.getDate(), dto.getTime()), null, false, dto.getPeriod(), dto.getNote()));
	}

	/**
	 * Finds an available table for the specified reservation DTO.
	 *
	 * @param dto the reservation data transfer object
	 * @return the available RestaurantTable entity
	 * @throws NoAvailableTablesTodayException if no tables are available for the specified date and period
	 */
	@SneakyThrows
	private RestaurantTable findAvailableTable(ReservationDTO dto) {
		for (RestaurantTable table : tableRepository.findAll()) {
			Optional<Reservation> res = table.getReservations().stream().filter(r -> r.getPeriod() == dto.getPeriod() && (r.getTime().toLocalDate().isEqual(dto.getDate()))).findFirst();
			if (!res.isPresent())
				return table;
		}
		throw new NoAvailableTablesTodayException(dto.getDate());
	}
}
