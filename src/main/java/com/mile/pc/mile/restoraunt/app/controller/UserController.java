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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mile.pc.mile.restoraunt.app.dto_dao.ReservationDTO;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.TableRepository;
import com.mile.pc.mile.restoraunt.app.service.CustomDTOservice;
import com.mile.pc.mile.restoraunt.app.service.MainService;

import lombok.SneakyThrows;

@Controller
@RequestMapping("/public")
public class UserController {

	@Autowired TableRepository tableRepo;
	@Autowired MainService main;
	@Autowired ReservationRepository rRepo;
	@Autowired CustomDTOservice des;
	
	@GetMapping(path = "/reserve-form")
	public ModelAndView reserveTableForm() {
		return new ModelAndView("public/reserve-form-user", "reservation", new ReservationDTO());
	}
	
	@PostMapping(path = "/reserveTable")
	public String reserveTable(@Valid @ModelAttribute ReservationDTO reservationDTO,RedirectAttributes redirAttrs) {
			main.reserveTable(reservationDTO);
			redirAttrs.addFlashAttribute("success", "Your reservation was successfull.");
			return "redirect:/home/";
	}
	
	@SneakyThrows
	@GetMapping(path ="/cancelReservation")
	public String cancelReservation(RedirectAttributes redirAttrs) {
		main.cancelReservation(SecurityContextHolder.getContext().getAuthentication().getName());
		redirAttrs.addFlashAttribute("success", "Your have successfully canceled your reservation.");

		return "redirect:/home/";
	}

}
