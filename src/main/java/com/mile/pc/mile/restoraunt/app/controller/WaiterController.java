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

	@PostMapping(path = "/setBusy")
	public String setTableToUnavailable(@RequestParam long id) {
		wS.setBusy(id);
		return "redirect:/tables";
	}
	@PostMapping(path ="/setCalm")
	public String setTableEmptyu(@RequestParam long id) {
		wS.setCalm(id);
		return "redirect:/tables";
	}
	@GetMapping(path = "/reservations")
	public List<Reservation> getReservations(){
		return reservations.findAll();
	}
	@GetMapping(path = "/setGuest")
	public String setTableAsGuest(@RequestParam long tableid) {
		wS.setGuestOnTable(tableid);
		return "redirect:/tables";
	}

}
