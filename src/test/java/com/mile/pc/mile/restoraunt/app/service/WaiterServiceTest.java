package com.mile.pc.mile.restoraunt.app.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.RestorauntTable;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.RoleRepository;
import com.mile.pc.mile.restoraunt.app.repo.TableRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class WaiterServiceTest {

	@InjectMocks 
	WaiterService service;

	@Mock RoleRepository roleRepository;
	@Mock TableRepository tRepo;
	@Mock ReservationRepository reservationRepository;
	@Mock UserRepository userRepository;

	@Test
	void testSetBusy() {
		if(LocalTime.now().isAfter(LocalTime.of(21, 0)))
			return;
		User user = new User(2l,"Mile", "username", "123", 600l, null, new HashSet<>(), LocalDateTime.now().minusDays(1));
		RestorauntTable table = new RestorauntTable(3l, false, new ArrayList<Reservation>());
		Reservation r = new Reservation(1l, true, user, table, LocalDateTime.now().plusMinutes(15), 300l, false, 2, null);
		r.setUTable(table);
		user.setReservation(r);
		when(tRepo.findById(table.getId())).thenReturn(Optional.of(table));
		service.setBusy(table.getId());
		assertAll(
				() -> assertEquals(true, table.getBusy()),
				() -> assertEquals(900l, user.getBalance())
				);
	}

	@Test
	void testSetCalm() {
		User user = new User(2l,"Mile", "username", "123", 600l, null, new HashSet<>(), LocalDateTime.now().minusDays(1));
		RestorauntTable table = new RestorauntTable(3l, true, new ArrayList<Reservation>());
		Reservation r = new Reservation(1l, true, user, table, LocalDateTime.now().plusMinutes(15), 300l, true, 2, null);
		r.setUTable(table);
		user.setReservation(r);		
		when(tRepo.findById(table.getId())).thenReturn(Optional.of(table));
		when(reservationRepository.findAll()).thenReturn(table.getReservations());
		when(reservationRepository.findById(r.getId())).thenReturn(Optional.of(r));
		service.setCalm(table.getId());
		assertAll(
				() -> assertEquals(null, user.getReservation()),
				() -> assertEquals(null, user.getReservationMoment()),
				() -> assertEquals(0, table.getReservations().size())
				);
	}

}
