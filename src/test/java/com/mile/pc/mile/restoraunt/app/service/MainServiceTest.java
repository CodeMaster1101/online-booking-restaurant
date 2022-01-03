package com.mile.pc.mile.restoraunt.app.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import com.mile.pc.mile.restoraunt.app.model.CustomTable;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
class MainServiceTest {
	
	@Autowired UserRepository uRepo;
	@Autowired MainService ms;
	@Autowired CustomTableRepository tRepo;
	ReservationRepository rRepo;
	@Test
	void testReserveTable() {
		Long id = tRepo.save(CustomTable.builder().id(88l).build()).getId();
		uRepo.save(User.builder().balance(200l).password("12345").username("mario").id(null)
				.build());
		Reservation reservation = rRepo.save(Reservation.builder()
				.id(null).user(uRepo.findByUsername("mario")).table(tRepo.findById(id).get())
				.time(LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 30))).build());
		ms.reserveTable(reservation);
		assertTrue(reservation.getUser().equals(uRepo.findByUsername("mario")));
		assertTrue(reservation.getTable().equals(tRepo.findById(id).get()));
	}

	@Test
	void testCancelReservation() {
		withSuccess();
	}

	@Test
	void testMainService() {
		withSuccess();
	}
	

}
