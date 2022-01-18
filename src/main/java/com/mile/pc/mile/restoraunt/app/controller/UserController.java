package com.mile.pc.mile.restoraunt.app.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mile.pc.mile.restoraunt.app.dto.ReservationDTO;
import com.mile.pc.mile.restoraunt.app.dto.ReservationOutro;
import com.mile.pc.mile.restoraunt.app.dto.UserPasswordForm;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.service.MainService;

@Controller
@RequestMapping("/public")
public class UserController {

	/*
	 * CLIENT CONTROLLER
	 */
	@Autowired CustomTableRepository tableRepo;
	@Autowired MainService main;
	@Autowired ReservationRepository rRepo;
	
	
	/*
	 * Getting the model methods
	 *
	 */
	
	@GetMapping(path = "/tables")
	public ModelAndView getTables(){
		return new ModelAndView("tables-user", "tables", tableRepo.findAll());
	}
	
	@GetMapping(path = "/viewTable")
	public ModelAndView getTableModel(@RequestParam Long id) {
		List<ReservationOutro> reservationsOutro = new ArrayList<>();
		List<Reservation> reservations = rRepo.findAll().stream()
		.filter(r -> r.getTable().getId() == id).collect(Collectors.toList());
		for (Reservation res : reservations) {
						reservationsOutro.add(new ReservationOutro(res.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), res.getMaxTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
		} 
		return new ModelAndView("view-table-user", "reservations", reservationsOutro);
	}
	
	@GetMapping(path = "/reserve-form")
	public ModelAndView reserveTableForm(@RequestParam long id) {
		return new ModelAndView("reserve-form-user", "reservation", new ReservationDTO(false,null, null, null, id));
	}
	
	@GetMapping(path = "/cancel-reservation-form")
	public ModelAndView getCancelingForm() {
		return new ModelAndView("cancel-form-user", "userPassword", new UserPasswordForm());
	}
	
	/*
	 * Executing the action
	 */
	
	@PostMapping(path = "/reserveTable")
	public String reserveTable(@ModelAttribute ReservationDTO reservationDTO) {
		main.reserveTable(reservationDTO);
		return "redirect:/public/tables";
	}
	@GetMapping(path ="/cancelReservation")
	public String cancelReservation(@ModelAttribute UserPasswordForm u_p_form) {
		if(main.cancelReservation(u_p_form))
		return "redirect:/public/tables";
		else 
			return"Oops! The time for canceling has expired. The reservation lives on. You can still attend to it.";

	}

}
