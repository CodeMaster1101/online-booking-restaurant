package com.mile.pc.mile.restoraunt.app.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mile.pc.mile.restoraunt.app.dto.ReservationDTO;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.service.CustomDTOservice;
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
	@Autowired CustomDTOservice dtoDes;
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
	
	@GetMapping(path = "/setBusy")
	public String setTableToUnavailable(@RequestParam long id) {
		wS.setBusy(id);
		return "redirect:/waiter/tables";
	}
	@GetMapping(path ="/setCalm")
	public String setTableEmpty(@RequestParam long id) {
		wS.setCalm(id);
		return "redirect:/waiter/tables";
	}
	
	@GetMapping(path = "/setGuest")
	public String setTableAsGuest(@RequestParam long id) {
		wS.setGuestBusy(id);
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
		ModelAndView mvc = new ModelAndView("all-reservations-waiter");
		mvc.addObject("noGuestReservations", dtoDes.reservationDTOconv(reservations.findAll()));
		mvc.addObject("guestReservations", dtoDes.guestReservations());
		return mvc;
	}
	
	@GetMapping(path ="/tableReservations")
	public ModelAndView tableReservations(@RequestParam long id) {
		Set<ReservationDTO> dto = dtoDes.reservationDTOconv(reservations.findAll().stream().filter(r->
		r.getTable().getId() == id).collect(Collectors.toSet()));
		return new ModelAndView("table-reservations-waiter", "reservations", dto);
	}
	@GetMapping(path = "/guestReservations")
	public ModelAndView allGuestReservations() {
		return new ModelAndView("table-reservations-waiter", "reservations", null);
	}
}
