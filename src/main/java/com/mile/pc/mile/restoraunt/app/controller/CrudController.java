package com.mile.pc.mile.restoraunt.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mile.pc.mile.restoraunt.app.dao.RoleToUser;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.RoleRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;
import com.mile.pc.mile.restoraunt.app.service.CrudService;

@Controller
@RequestMapping("/admin")
public class CrudController {

	/*
	 * ADMIN CONTROLLER
	 */
	@Autowired CrudService crudService;
	@Autowired UserRepository uRepo;
	@Autowired RoleRepository rRepo;
	@Autowired CustomTableRepository tRepo;

	/*
	 * Home page the administrator(MENU)
	 */
	@GetMapping(path = {"","/home"})
	public ModelAndView adminHome() {
		return new ModelAndView("home-admin");
	}

	/*
	 * Getter methods for Models -> Methods that return Model based value
	 */
	@GetMapping(path = "/roles")
	public ModelAndView getRoles(){
		return new ModelAndView("roles-admin", "roles", rRepo.findAll());
	}
	@GetMapping(path = "/users")
	public ModelAndView allUsers() {
		return new ModelAndView("users-admin","users", uRepo.findAll());
	}
	@GetMapping(path = "users/waiters")
	public ModelAndView getWaiters(){
		return new ModelAndView("waiters-admin","waiters", crudService.getWaiters());
	}
	@GetMapping(path = "/add-RTU-form")
	public ModelAndView addRoleToUserForm(@RequestParam long id) {
		return new ModelAndView("add-role-to-user-admin", "rtu", 
				new RoleToUser(null,  uRepo.findById(id).get().getUsername()));
	}
	@GetMapping(path = "/remove-RFU-form")
	public ModelAndView removeRoleFromUser(@RequestParam long id) {
		return new ModelAndView("remove-role-from-user-admin", "rfu", 
				new RoleToUser(null,  uRepo.findById(id).get().getUsername()));
	}
	@GetMapping(path = "/addUser-form")
	public ModelAndView addUserForm() {
		return new ModelAndView("add-user-form-admin", "user", new User());
	}
	@GetMapping(path = "/updateUser")
	public ModelAndView getUpdateOptions(@RequestParam long id) {
		return new ModelAndView("update-admin","user", uRepo.findById(id).get());
	}
	
	/*
	 * Methods that change the DB
	 */
	
	@PostMapping(path = "/add-RTU")
	public String addRoleToUser(@ModelAttribute RoleToUser rtu) {
		crudService.AddRoleToUser(rtu.getUsername(), rtu.getType());
		return "redirect:/admin/users";
	}
	@GetMapping(path = "/remove-RFU")
	public String removeRoleFromUser(@ModelAttribute RoleToUser rtu) {
		crudService.removeRolefromUser(rtu.getUsername(), rtu.getType());
		return "redirect:/admin/users";
	}
	@PostMapping(path = "/saveUser")
	public String addUser(@ModelAttribute User user) {
		crudService.addUser(user);
		return "redirect:/admin/users";
	}
	@GetMapping(path = "/deleteUser")
	public String deleteUser(@RequestParam long id) {
		crudService.removeUser(id);
		return "redirect:/admin/users";
	}

}
