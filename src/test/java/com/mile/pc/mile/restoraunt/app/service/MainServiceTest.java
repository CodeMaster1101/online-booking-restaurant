package com.mile.pc.mile.restoraunt.app.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.mile.pc.mile.restoraunt.app.dto.ReservationDTO;
import com.mile.pc.mile.restoraunt.app.model.CustomTable;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class MainServiceTest {

	@InjectMocks MainService main;
	@Mock UserRepository userRepository;
	@Mock ReservationRepository reservationRepository;
	@Mock CustomTableRepository customTableRepository;
	
	@Test
	void testReserveTable() {
		ReservationDTO dto = new ReservationDTO(true, "mark", "123", LocalDateTime.of(
				LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth()),
				LocalTime.of(12, 0)), LocalTime.of(14, 30), 3l);
		User user = new User(2L, "mark", "123", 1500, null, new HashSet<>(), null);
		CustomTable table = new CustomTable(3l, false, new ArrayList<>());
		Reservation reservation = new Reservation(1l, true, user, table, dto.getTime(), LocalDateTime.of(dto.getTime().toLocalDate(), dto.getMaxTime()), 0, null, 300l);
		
		//3
		doAnswer(invocation->{
			ReflectionTestUtils.setField((Reservation) invocation.getArgument(0), "id", 1l);
			return null;
		}).when(reservationRepository.save(any(Reservation.class)));
		
		//2
		when(reservationRepository.save(any(Reservation.class))).then(invocation->{
			ReflectionTestUtils.setField((Reservation) invocation.getArgument(0), "id", 1l);
			return null;
		}).thenReturn(reservation);
		
		//1
		when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
		
		when(customTableRepository.getById(3l)).thenReturn(table);
		when(userRepository.findByUsername("mark")).thenReturn(user);
		
		main.reserveTable(dto);
		verify(reservationRepository, times(1)).save(reservation);
	}

	@Test
	void testCancelReservation() {
		withSuccess();
	}

	@Test
	void testCheckOtherReservations() {
		withSuccess();
	}

}
