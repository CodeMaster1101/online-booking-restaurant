package com.mile.pc.mile.restoraunt.app.service;

import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

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
		withSuccess();
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
