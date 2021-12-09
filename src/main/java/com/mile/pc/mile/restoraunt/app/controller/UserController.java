package com.mile.pc.mile.restoraunt.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mile.pc.mile.restoraunt.app.dao.UserPasswordForm;
import com.mile.pc.mile.restoraunt.app.model.CustomTable;
import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.service.MainService;

@RestController
@RequestMapping("/public")
public class UserController {

	/*
	 * CLIENT CONTROLLER
	 */
	@Autowired CustomTableRepository tableRepo;
	@Autowired MainService main;

	@GetMapping(path = "/tables")
	public List<CustomTable> getTables(){
		return tableRepo.findAll();
	}
	@GetMapping(path = "/reserveTable-form")
	public Optional<CustomTable> getTableModel(@RequestParam Long id) {
		return tableRepo.findById(id);
	}

	@PostMapping(path = "/reserveTable")
	public String reserveTable(@RequestBody Reservation reservation) {
		main.reserveTable(reservation);
		return "redirect:/tables";
	}
	@DeleteMapping(path ="/cancelReservation")
	public String cancelReservation(@RequestBody UserPasswordForm u_p_form) {
		main.cancelReservation(u_p_form);
		return "redirect:/tables";

	}
}
