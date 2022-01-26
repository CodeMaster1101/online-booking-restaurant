package com.mile.pc.mile.restoraunt.app.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mile.pc.mile.restoraunt.app.dto.publi.ReservationDTO;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.service.DTOserDes;
import com.mile.pc.mile.restoraunt.app.service.WaiterService;

/*
 * SEMI ADMIN CONTROLLER
 */

@Controller
@RequestMapping("/waiter")
public class WaiterController {

	@Autowired WaiterService wS;
	@Autowired ReservationRepository reservations;
	@Autowired CustomTableRepository customTableRepository;
	@Autowired DTOserDes dtoDes;
	/*
	 * Model fetching methods 
	 */
	
	@GetMapping(path = {"/tables", ""})
	public ModelAndView allTables() {
		return new ModelAndView("tables-waiter", "tables", customTableRepository.findAll());
	}
	
	/*
	 * Table tool methods
	 */
	
	@PostMapping(path = "/setBusy")
	public String setTableToUnavailable(@RequestParam long id) {
		wS.setBusy(id);
		return "redirect:/waiter/tables";
	}
	@DeleteMapping(path ="/setCalm")
	public String setTableEmpty(@RequestParam long id) {
		wS.setCalm(id);
		return "redirect:/waiter/tables";
	}
	
	@PostMapping(path = "/setGuest")
	public String setTableAsGuest(@RequestParam long tableid) {
		wS.setGuestBusy(tableid);
		return "redirect:/waiter/tables";
	}

	/**
	 * NAVBAR http methods
	 */
	
	@GetMapping(path = "deleteExpiredReservations")
	public String deleteExpiredReservations() {
		wS.removeExpiredReservations();
		return "redirect:/waiter/tables";
	}
	
	@GetMapping(path ="/todayReservations")
	public ModelAndView tReservations(){
		return new ModelAndView("today-waiter", "reservations", dtoDes.reservationDTOconv(wS.todayReservations()));
	}
	
	@GetMapping(path = "/reservations")
	public ModelAndView getReservations(){
		return new ModelAndView("all-reservations-waiter", "reservations", dtoDes.reservationDTOconv(reservations.findAll()));
	}
	
	@GetMapping(path ="/tableReservations")
	public ModelAndView tableReservations(@RequestParam long id) {
		Set<ReservationDTO> dto = dtoDes.reservationDTOconv(reservations.findAll().stream().filter(r->
		r.getTable().getId() == id).collect(Collectors.toSet()));
		return new ModelAndView("table-reservations-waiter", "reservations", dto);
	}
}
