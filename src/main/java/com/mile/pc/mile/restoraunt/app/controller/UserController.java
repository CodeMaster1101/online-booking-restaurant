package com.mile.pc.mile.restoraunt.app.controller;

import com.mile.pc.mile.restoraunt.app.dto_dao.ReservationDTO;
import com.mile.pc.mile.restoraunt.app.service.MainService;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/public")
final class UserController {

	private final MainService mainService;

  UserController(MainService mainService) {
    this.mainService = mainService;
  }

  @GetMapping(path = "/reserve-form")
	public ModelAndView reserveTableForm() {
		return new ModelAndView("public/reserve-form-user", "reservation", new ReservationDTO());
	}

	@PostMapping(path = "/reserveTable")
	public String reserveTable(@Valid @ModelAttribute ReservationDTO reservationDTO,RedirectAttributes redirectAttributes) {
			mainService.reserveTable(reservationDTO);
		redirectAttributes.addFlashAttribute("success", "Your reservation was successful.");
			return "redirect:/home/";
	}

	@SneakyThrows
	@GetMapping(path ="/cancelReservation")
	public String cancelReservation(RedirectAttributes redirectAttributes) {
		mainService.cancelReservation(SecurityContextHolder.getContext().getAuthentication().getName());
		redirectAttributes.addFlashAttribute("success", "Your have successfully canceled your reservation.");

		return "redirect:/home/";
	}

}
