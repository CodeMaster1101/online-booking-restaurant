package com.mile.pc.mile.restoraunt.app.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mile.pc.mile.restoraunt.app.dto.ReservationDTO;
import com.mile.pc.mile.restoraunt.app.dto.ReservationOutro;
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
	public Set<ReservationDTO> reservationDTOconv(Collection<Reservation> coll){
		Set<ReservationDTO> dto = new HashSet<>();
		coll.forEach(e -> {
			dto.add(new ReservationDTO(e.getAccepted(), null,
					null, e.getTime(), e.getMaxTime().toLocalTime(), e.getTable().getId()));
		});
		return dto;
	}

	public Set<ReservationDTO> reservationDTOconvNoGuest(Collection<Reservation> coll){
		Set<ReservationDTO> dto = new HashSet<>();
		coll.forEach(e -> {
			if(e.getUser() != null) {
				dto.add(new ReservationDTO(e.getAccepted(), null,
						null, e.getTime(), e.getMaxTime().toLocalTime(), e.getTable().getId()));dto.add(new ReservationDTO(e.getAccepted(), null,
								null, e.getTime(), e.getMaxTime().toLocalTime(), e.getTable().getId()));
			}			
		});
		return dto;
	}

	public List<ReservationOutro> reservationTimes(long id){
		List<ReservationOutro> reservationsOutro = new ArrayList<>();
		List<Reservation> resevations = rRepo.findAll().stream()
				.filter(r -> r.getTable().getId() == id).collect(Collectors.toList());
		resevations.forEach(r-> {
			reservationsOutro.add(new ReservationOutro(r.getTable().getId(), r.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), 
					r.getMaxTime().toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME)));

		});
		return reservationsOutro;
	}
}
