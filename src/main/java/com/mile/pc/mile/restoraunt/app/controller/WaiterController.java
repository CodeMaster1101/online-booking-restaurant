package com.mile.pc.mile.restoraunt.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
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
		return new ModelAndView("waiter/tables-waiter", "tables", customTableRepository.findAll());
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
		return new ModelAndView("waiter/today-waiter", "reservations", dtoDes.reservationDTOconv(wS.todayReservations()));
	}
	
	

}
