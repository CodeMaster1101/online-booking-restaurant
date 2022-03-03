package com.mile.pc.mile.restoraunt.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mile.pc.mile.restoraunt.app.repo.TableRepository;
import com.mile.pc.mile.restoraunt.app.service.CustomDTOservice;
import com.mile.pc.mile.restoraunt.app.service.WaiterService;

@Controller
@RequestMapping("/waiter")
public class WaiterController {

	@Autowired WaiterService wS;
	@Autowired TableRepository tableRepo;
	@Autowired CustomDTOservice dtoService;

	@GetMapping(path = {"/tables", ""})
	public ModelAndView allTables() {
		return new ModelAndView("waiter/tables-waiter", "tables", tableRepo.findAll());
	}
	
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

	@GetMapping(path = "deleteExpiredReservations")
	public String deleteExpiredReservations() {
		wS.removeExpiredReservations();
		return "redirect:/waiter/tables";
	}
	
	@GetMapping(path ="/todayReservations")
	public ModelAndView tReservations(){
		return new ModelAndView("waiter/today-waiter", "reservations", dtoService.reservationDTOconv(wS.todayReservations()));
	}
	
}
