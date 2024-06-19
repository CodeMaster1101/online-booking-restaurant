package com.mile.pc.mile.restoraunt.app.service;

import com.mile.pc.mile.restoraunt.app.dto_dao.ReservationWaiterDAO;
import com.mile.pc.mile.restoraunt.app.dto_dao.UserDAO;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.Role;
import com.mile.pc.mile.restoraunt.app.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This service class provides custom Data Transfer Object (DTO) conversions for various entities.
 * It helps in transforming complex entity models into simpler DTOs for easier consumption in other layers.
 *
 * @see User
 * @see UserDAO
 * @see Reservation
 * @see ReservationWaiterDAO
 *
 * @autor Mile Stanislavov
 */
@Service
public final class CustomDTOService {

	/**
	 * Extracts the roles of a given user.
	 *
	 * @param user the user entity from which roles are extracted
	 * @return a set of role types associated with the user
	 */
	private Set<String> roles(User user) {
		return user.getRoles().stream().map(Role::getType).collect(Collectors.toSet());
	}

	/**
	 * Converts a list of User entities to a set of UserDTO objects.
	 *
	 * @param repoUsers the list of User entities to be converted
	 * @return a set of UserDTO objects
	 */
	public Set<UserDAO> usersDTO(List<User> repoUsers) {
		return repoUsers.stream().map(u -> new UserDAO(u.getId(), u.getFirstName(), u.getUsername(), u.getBalance(), roles(u))).collect(Collectors.toSet());
	}

	/**
	 * Converts a list of Reservation entities to a set of ReservationWaiterDTO objects.
	 *
	 * @param todayReservations the list of Reservation entities to be converted
	 * @return a set of ReservationWaiterDTO objects
	 */
	public Set<ReservationWaiterDAO> reservationDTOConversion(List<Reservation> todayReservations) {
		return todayReservations.stream().map(r -> new ReservationWaiterDAO(r.getTable().getId(),
				r.getUser().getFirstName(), r.getUser().getUsername(), fetchPeriodAsString(r), r.getTime().toLocalTime(), r.isBusy(), r.getNote())).collect(Collectors.toSet());
	}

	/**
	 * Converts the period of a reservation to a corresponding string representation.
	 *
	 * @param reservation the reservation entity whose period is to be converted
	 * @return a string representation of the reservation period (e.g., "Breakfast", "Lunch", "Dinner")
	 */
	private String fetchPeriodAsString(Reservation reservation) {
		switch (reservation.getPeriod()) {
		case 1: return "Breakfast";
		case 2: return "Lunch";
		case 3: return "Dinner";
		default: return null;
		}
	}
}
