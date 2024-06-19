package com.mile.pc.mile.restoraunt.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.mile.pc.mile.restoraunt.app.repo.TableRepository;
import com.mile.pc.mile.restoraunt.app.service.CustomDTOService;
import com.mile.pc.mile.restoraunt.app.service.WaiterService;

@Controller
@RequestMapping("/waiter")
final class WaiterController {

	private final WaiterService waiterService;
	private final TableRepository tableRepository;
	private final CustomDTOService dtoService;

  WaiterController(WaiterService waiterService, TableRepository tableRepository, CustomDTOService dtoService) {
    this.waiterService = waiterService;
    this.tableRepository = tableRepository;
    this.dtoService = dtoService;
  }

  @GetMapping(path = {"/tables", ""})
	public ModelAndView allTables() {
		return new ModelAndView("waiter/tables-waiter", "tables", tableRepository.findAll());
	}

	@GetMapping(path = "/setBusy")
	public String setTableToUnavailable(@RequestParam long id, RedirectAttributes redirectAttributes) {
		waiterService.setBusy(id);
		redirectAttributes.addFlashAttribute("success", "Table " + id + " has been OCCUPIED!");
		return "redirect:/waiter/tables";
	}

	@GetMapping(path ="/setCalm")
	public String setTableEmpty(@RequestParam long id, RedirectAttributes redirectAttributes) {
		waiterService.setCalm(id);
		redirectAttributes.addFlashAttribute("success", "Table " + id + " is now EMPTY");
		return "redirect:/waiter/tables";
	}

	@GetMapping(path = "deleteExpiredReservations")
	public String deleteExpiredReservations(RedirectAttributes redirectAttributes) {
		if(waiterService.removeExpiredReservations() >= 1) {
			redirectAttributes.addFlashAttribute("success", "Your have successfully removed expired reservations.");
		}else
			redirectAttributes.addFlashAttribute("success", "No expired reservations at the moment.");
		return "redirect:/waiter/tables";
	}

	@GetMapping(path ="/todayReservations")
	public ModelAndView tReservations(){
		return new ModelAndView(
				"waiter/today-waiter",
				"reservations",
				dtoService.reservationDTOConversion(waiterService.todayReservations()));
	}
	
}
