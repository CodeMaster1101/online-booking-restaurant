package com.mile.pc.mile.restoraunt.app.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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

import com.mile.pc.mile.restoraunt.app.exceptions.TimeOutForCancelingException;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.RestorauntTable;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.TableRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;


@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class MainServiceTest {

	@InjectMocks MainService mainService;
	@Mock TableRepository customTableRepository;
	@Mock UserRepository userRepository;
	@Mock ReservationRepository reservationRepository;
	
	@Test
	void testCancelReservationWhenUserLate() throws TimeOutForCancelingException {
		try {
			RestorauntTable table = new RestorauntTable(2l,false,new ArrayList<>());
			Reservation r = new Reservation(3l, true, null, null, LocalDateTime.now().plusDays(1), 400l, false, 3, "");
			r.setUTable(table);
			User user = new User(1l,"Mile", "username","123",800,r,new HashSet<>(),LocalDateTime.now().minusMinutes(12));
			r.setUser(user);
			when(userRepository.getUserByUsername(user.getUsername())).thenReturn(user);
			mainService.cancelReservation(user.getUsername());
		} 
		catch (TimeOutForCancelingException e) {
			assertTrue(e.error().contains("hours have passed since the moment of your reservation"));
		}

	}

	@Test 
	void testCancelReservationWhenUserOnTime() throws TimeOutForCancelingException{
	
		RestorauntTable table = new RestorauntTable(2l,false,new ArrayList<>());
		Reservation r = new Reservation(3l, true, null, null, LocalDateTime.now().plusDays(1), 400l, false, 3, "");
		r.setUTable(table);
		User user = new User(1l,"Mile", "username","123",800,r,new HashSet<>(),LocalDateTime.now().minusMinutes(7));
		r.setUser(user);
		Mockito.when(userRepository.getUserByUsername(user.getUsername())).thenReturn(user);
		mainService.cancelReservation(user.getUsername());
		assertAll(
				() -> assertEquals(null, user.getReservation()),
				() -> assertEquals(0, table.getReservations().size()));

	}
}
