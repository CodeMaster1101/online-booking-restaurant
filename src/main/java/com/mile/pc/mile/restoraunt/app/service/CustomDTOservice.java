package com.mile.pc.mile.restoraunt.app.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mile.pc.mile.restoraunt.app.dto.ReservationDTO;
import com.mile.pc.mile.restoraunt.app.dto.UserDTO;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.RoleRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

@Service
public class CustomDTOservice {

	@Autowired JdbcTemplate jdbcTemplate;
	@Autowired ReservationRepository rRepo;
	@Autowired UserRepository uRepository;
	@Autowired RoleRepository roleRepository;
	
	private Set<String> roles(User user){
		return user.getRoles().stream().map(r -> r.getType()).collect(Collectors.toSet());
	}
	public Set<UserDTO> usersDTO(List<User> repoUsers){
		Set<UserDTO> users = new HashSet<>();
		repoUsers.forEach(u->{
			users.add(new UserDTO(u.getId(), u.getUsername(), u.getBalance(), roles(u)));
		});
		return users;
	}
	public Set<ReservationDTO> reservationDTOconv(List<Reservation> todayReservations) {
		return null;
	}
}
