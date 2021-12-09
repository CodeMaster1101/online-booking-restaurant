package com.mile.pc.mile.restoraunt.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.service.WaiterService;

@RestController
@RequestMapping("/waiter")
public class WaiterController {

	@Autowired WaiterService wS;
	@Autowired ReservationRepository reservations;
	/*
	 * SEMI ADMIN CONTROLLER
	 */

	@PostMapping(path = "/setAvailable")
	public String setTableToAvailable(@RequestParam long id ) {
		wS.setAvailable(id);
		return "redirect:/tables";
	}
	@PostMapping(path = "/setUnavailable")
	public String setTableToUnavailable(@RequestParam long id ) {
		wS.setBusy(id);
		return "redirect:/tables";
	}
	@GetMapping(path = "/reservations")
	List<Reservation> getReservations(){
		return reservations.findAll();
	}
	@PostMapping(path = "/setUnavailableNoReser")
	public String setAvailableNoReser(@RequestParam long id) {
	   wS.setAvailableNoReser(id);
	   return "redirect:/tables";
	}
}
