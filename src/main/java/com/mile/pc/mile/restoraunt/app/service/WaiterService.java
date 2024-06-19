package com.mile.pc.mile.restoraunt.app.service;

import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;
import com.mile.pc.mile.restoraunt.app.exceptions.AlreadyBusyException;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.RestaurantTable;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.RoleRepository;
import com.mile.pc.mile.restoraunt.app.repo.TableRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The WaiterService class manages the actions and responsibilities of the waiter in the restaurant.
 * The waiter is responsible for:
 * <p>
 * - Viewing all tables in the restaurant (ID, Occupied/Not, Actions).
 * - Viewing today's reservations.
 * - Removing all "expired reservations" and transferring fees to the admin's wallet.
 * - Managing table statuses (busy/calm).
 * </p>
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
public class WaiterService {

	private final RoleRepository roleRepository;
	private final TableRepository tableRepository;
	private final ReservationRepository reservationRepository;
	private final UserRepository userRepository;

	/**
	 * Constructor to initialize repositories for role, table, reservation, and user entities.
	 *
	 * @param roleRepository the repository to manage Role entities
	 * @param tableRepository the repository to manage RestaurantTable entities
	 * @param reservationRepository the repository to manage Reservation entities
	 * @param userRepository the repository to manage User entities
	 */
	public WaiterService(RoleRepository roleRepository, TableRepository tableRepository, ReservationRepository reservationRepository,
			UserRepository userRepository) {
		this.roleRepository = roleRepository;
		this.tableRepository = tableRepository;
		this.reservationRepository = reservationRepository;
		this.userRepository = userRepository;
	}

	/**
	 * Informs the service that a user with a reservation has just arrived and sets the table as busy.
	 * If the table is already busy or the user arrives too early or late, an exception is thrown.
	 *
	 * @param tableId the ID used to find the specific table from the repository
	 * @throws AlreadyBusyException if the table is already busy
	 */
	@SneakyThrows @Transactional
	public void setBusy(long tableId) {
		Optional<RestaurantTable> optionalTable = tableRepository.findById(tableId);
		if (optionalTable.isPresent()) {
			RestaurantTable table = optionalTable.get();
			if (!table.getBusy()) {
				Reservation currentRes = findCurrentReservation(table);
				reservationBusyLogic(currentRes);
				table.setBusy(true);
				return;
			}
			throw new AlreadyBusyException(tableId);
		} else {
			throw new IllegalArgumentException("Table not found for id: " + tableId);
		}
	}

	/**
	 * Informs the service that the current client has called for a check or has left the table, marking it as calm.
	 *
	 * @param tableId the ID of the table to be marked as calm
	 */
	@Transactional
	public void setCalm(long tableId) {
		Optional<RestaurantTable> optionalTable = tableRepository.findById(tableId);
		if (optionalTable.isPresent()) {
			RestaurantTable table = optionalTable.get();
			Optional<Reservation> optionalReservation = reservationRepository.findAll().stream()
					.filter(res -> res.getTable().equals(table) && res.isBusy()).findFirst();
			if (optionalReservation.isPresent()) {
				emptyTableAndReservations(table, optionalReservation.get());
			} else {
				throw new IllegalArgumentException("No busy reservation found for table id: " + tableId);
			}
		} else {
			throw new IllegalArgumentException("Table not found for id: " + tableId);
		}
	}

	/**
	 * Retrieves the list of today's reservations.
	 *
	 * @return a list of reservations for the current day
	 */
	public List<Reservation> todayReservations() {
		return reservationRepository.findAll().stream()
				.filter(r -> r.getTime().toLocalDate().isEqual(LocalDate.now()))
				.collect(Collectors.toList());
	}

	/**
	 * Removes all expired reservations and transfers their fees to the admin's wallet.
	 *
	 * @return the number of expired reservations removed
	 */
	@Transactional
	public int removeExpiredReservations() {
		int count = 0;
		List<Reservation> expiredReservations = reservationRepository.findAll().stream()
				.filter(r -> !r.isBusy() && r.getTime().plusMinutes(CONSTANTS.AFTER_RESERVATION_TIME).isBefore(LocalDateTime.now()))
				.collect(Collectors.toList());
		for (Reservation reservation : expiredReservations) {
			sendRefundToAdmin(reservation);
			count++;
		}
		return count;
	}

	/**
	 * Removes a reservation by its ID.
	 *
	 * @param id the ID of the reservation to be removed
	 */
	private void removeReservation(long id) {
		Optional<Reservation> optionalReservation = reservationRepository.findById(id);
		if (optionalReservation.isPresent()) {
			Reservation reservation = optionalReservation.get();
			reservation.getUser().setReservation(null);
			reservation.getUser().setReservationMoment(null);
			reservation.getTable().removeReservation(reservation);
		} else {
			throw new IllegalArgumentException("Reservation not found for id: " + id);
		}
	}

	/**
	 * Transfers the fee of an expired reservation to the admin's wallet and removes the reservation.
	 *
	 * @param reservation the reservation whose fee is to be transferred
	 */
	private void sendRefundToAdmin(Reservation reservation) {
		if (reservation.getFee() != null) {
			Optional<User> optionalAdmin = userRepository.findAll().stream()
					.filter(u -> u.getRoles().contains(roleRepository.findByType("ADMIN"))).findFirst();
			if (optionalAdmin.isPresent()) {
				User admin = optionalAdmin.get();
				admin.setBalance(admin.getBalance() + reservation.getFee());
				removeReservation(reservation.getId());
			} else {
				throw new IllegalArgumentException("Admin user not found");
			}
		} else {
			removeReservation(reservation.getId());
		}
	}

	/**
	 * Finds the current reservation for a table based on the current time.
	 *
	 * @param table the table to find the reservation for
	 * @return the current reservation for the table
	 * @throws IllegalArgumentException if no current reservation is found
	 */
	private Reservation findCurrentReservation(RestaurantTable table) {
		LocalTime now = LocalTime.now();
		Optional<Reservation> optionalReservation = table.getReservations().stream()
				.filter(r -> r.getTime().toLocalDate().isEqual(LocalDate.now())
						&& now.isAfter(r.getTime().toLocalTime().minusMinutes(CONSTANTS.BEFORE_RESERVATION_TIME))
						&& now.isBefore(r.getTime().toLocalTime().plusMinutes(CONSTANTS.AFTER_RESERVATION_TIME)))
				.findFirst();
		return optionalReservation.orElseThrow(() -> new IllegalArgumentException("No current reservation found for table id: " + table.getId()));
	}

	/**
	 * Empties the table and marks the reservation as completed.
	 *
	 * @param table the table to be emptied
	 * @param currentReservation the current reservation to be marked as completed
	 */
	private void emptyTableAndReservations(RestaurantTable table, Reservation currentReservation) {
		removeReservation(currentReservation.getId());
		table.setBusy(false);
	}

	/**
	 * Marks a reservation as busy and updates the user's balance.
	 *
	 * @param reservation the reservation to be marked as busy
	 */
	private void reservationBusyLogic(Reservation reservation) {
		reservation.getUser().setBalance(reservation.getUser().getBalance() + reservation.getFee());
		reservation.setFee(null);
		reservation.setBusy(true);
	}
}
