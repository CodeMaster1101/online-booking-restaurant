package com.mile.pc.mile.restoraunt.app.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mile.pc.mile.restoraunt.app.dto.ReservationWaiterDTO;
import com.mile.pc.mile.restoraunt.app.dto.UserDTO;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.RoleRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

import lombok.SneakyThrows;

@Service
public class CustomDTOservice {

	@Autowired ReservationRepository rRepo;
	@Autowired UserRepository uRepository;
	@Autowired RoleRepository roleRepository;

	private Set<String> roles(User user){
		return user.getRoles().stream().map(r -> r.getType()).collect(Collectors.toSet());
	}
	
	public Set<UserDTO> usersDTO(List<User> repoUsers){
		return repoUsers.stream().map(u-> new UserDTO(u.getId(), u.getUsername(), u.getBalance(), roles(u))).collect(Collectors.toSet());
	}
	
	public Set<ReservationWaiterDTO> reservationDTOconv(List<Reservation> todayReservations) {

		return todayReservations.stream().map(r-> new ReservationWaiterDTO(r.getTable().getId(),
				r.getUser().getUsername(), fetchPeriodAsString(r), r.getTime().toLocalTime(), r.getNote())).collect(Collectors.toSet());
	}

	private String fetchPeriodAsString(Reservation reservation) {
		switch (reservation.getPeriod()) {
		case 1: return "Breakfast";
		case 2: return "Lunch";
		case 3: return "Dinner";
		}
		return null;
	}
}
