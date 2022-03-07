package com.mile.pc.mile.restoraunt.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mile.pc.mile.restoraunt.app.dto_dao.SignUpDTO;
import com.mile.pc.mile.restoraunt.app.security.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/security")
public class SecurityController {

	@Autowired UserService userService;

	@GetMapping(path = {"/login", ""})
	public ModelAndView login() {
		return new ModelAndView("security/login-security");
	}

	@GetMapping(path = "/signUp")
	public ModelAndView signUp() {
		return new ModelAndView("security/sign-up-security","dto",new SignUpDTO());
	}

	@PostMapping(path = "/saveUser")
	public String addUser(@Valid @ModelAttribute SignUpDTO signUpDTO) {
		 userService.save(signUpDTO);
		return "redirect:/security/login?success";
	}
}
