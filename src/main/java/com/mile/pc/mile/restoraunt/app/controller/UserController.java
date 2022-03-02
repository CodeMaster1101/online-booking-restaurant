package com.mile.pc.mile.restoraunt.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mile.pc.mile.restoraunt.app.dto.ReservationDTO;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.service.CustomDTOservice;
import com.mile.pc.mile.restoraunt.app.service.MainService;

import lombok.SneakyThrows;

@Controller
@RequestMapping("/public")
public class UserController {

	/*
	 * CLIENT CONTROLLER
	 */
	@Autowired CustomTableRepository tableRepo;
	@Autowired MainService main;
	@Autowired ReservationRepository rRepo;
	@Autowired CustomDTOservice des;
	
	/*
	 * Getting the model methods
	 *
	 */
	@GetMapping(path = {"", "/home"})
	public ModelAndView homePage() {
		return new ModelAndView("index");
	}
	@GetMapping(path = "/reserve-form")
	public ModelAndView reserveTableForm() {
		return new ModelAndView("public/reserve-form-user", "reservation", new ReservationDTO());
	}
	
	/*
	 * Executing the action
	 */
	
	@PostMapping(path = "/reserveTable")
	public String reserveTable(@Valid @ModelAttribute ReservationDTO reservationDTO) {
			main.reserveTable(reservationDTO);
			return "redirect:/public/home";
	}
	@SneakyThrows
	@GetMapping(path ="/cancelReservation")
	public String cancelReservation() {
		main.cancelReservation(SecurityContextHolder.getContext().getAuthentication().getName());
		return "redirect:/public/home";
	}

}
