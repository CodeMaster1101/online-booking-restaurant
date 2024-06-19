package com.mile.pc.mile.restoraunt.app.controller;

import com.mile.pc.mile.restoraunt.app.dto_dao.RoleToUser;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;
import com.mile.pc.mile.restoraunt.app.service.AdminService;
import com.mile.pc.mile.restoraunt.app.service.CustomDTOService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
final class AdminController {

	
	private final AdminService adminService;
	private final UserRepository userRepository;
	private final CustomDTOService dtoService;

  AdminController(AdminService adminService, UserRepository userRepository, CustomDTOService dtoService) {
    this.adminService = adminService;
    this.userRepository = userRepository;
    this.dtoService = dtoService;
  }

  @GetMapping(path = "/users")
	public ModelAndView allUsers() {
		return new ModelAndView("admin/users-admin","users", dtoService.usersDTO(userRepository.findAll()));
	}

	@GetMapping(path = "users/waiters")
	public ModelAndView getWaiters() {
		return new ModelAndView("admin/waiters-admin","waiters", dtoService.usersDTO(adminService.getWaiters()));
	}

	@GetMapping(path = "/add-RTU-form")
	public ModelAndView addRoleToUserForm(@RequestParam long id) {
		return new ModelAndView("admin/add-role-to-user-admin", "rtu", 
				new RoleToUser(null,  adminService.getUsernameById(id)));
	}

	@GetMapping(path = "/remove-RFU-form")
	public ModelAndView removeRoleFromUser(@RequestParam long id) {
		return new ModelAndView("admin/remove-role-from-user-admin", "rfu", 
				new RoleToUser(null, adminService.getUsernameById(id)));
	}

	@GetMapping(path = "/updateUser")
	public ModelAndView getUpdateOptions(@RequestParam long id) {
		return new ModelAndView("admin/update-admin","user", adminService.getUserById(id));
	}

	/*
	 * Methods that change the DB
	 */

	@PostMapping(path = "/add-RTU")
	public String addRoleToUser(@ModelAttribute RoleToUser dto) {
		adminService.addRoleToUser(dto.getUsername(), dto.getType());
		return "redirect:/admin/users";
	}

	@GetMapping(path = "/remove-RFU")
	public String removeRoleFromUser(@ModelAttribute RoleToUser dto) {
		adminService.removeRoleFromUser(dto.getUsername(), dto.getType());
		return "redirect:/admin/users";
	}

	@GetMapping(path = "/deleteUser")
	public String deleteUser(@RequestParam long id) {
		adminService.removeUser(id);
		return "redirect:/admin/users";
	}

}
