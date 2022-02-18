package com.mile.pc.mile.restoraunt.app.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.mile.pc.mile.restoraunt.app.dto.UserPasswordForm;
import com.mile.pc.mile.restoraunt.app.model.CustomTable;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class MainServiceTest {

	@InjectMocks MainService mainService;
	@Mock CustomTableRepository customTableRepository;
	@Mock UserRepository userRepository;
	@Mock ReservationRepository reservationRepository;

	@Test
	void testCancelReservationWhenUserLate() {
		try {
			CustomTable table = new CustomTable(2l,false,new ArrayList<>());
			Reservation r = new Reservation(3l, true, null, null, LocalDateTime.now().plusDays(1), 400l, false, 3, "");
			r.setUTable(table);
			User user = new User(1l,"username","123",800,r,new HashSet<>(),LocalDateTime.now().minusMinutes(12));
			r.setUser(user);
			UserPasswordForm upf = new UserPasswordForm("username", "123");
			Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
			mainService.cancelReservation(upf);
		} 
		catch (Exception e) {
			assertTrue(e.getMessage() == "time out!");
		}

	}

	@Test
	void testCancelReservationWhenUserOnTime() {
		CustomTable table = new CustomTable(2l,false,new ArrayList<>());
		Reservation r = new Reservation(3l, true, null, null, LocalDateTime.now().plusDays(1), 400l, false, 3, "");
		r.setUTable(table);
		User user = new User(1l,"username","123",800,r,new HashSet<>(),LocalDateTime.now().minusMinutes(7));
		r.setUser(user);
		UserPasswordForm upf = new UserPasswordForm("username", "123");
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		mainService.cancelReservation(upf);
		assertAll(
				() -> assertEquals(null, user.getReservation()),
				() -> assertEquals(0, table.getReservations().size()));

	}


}
