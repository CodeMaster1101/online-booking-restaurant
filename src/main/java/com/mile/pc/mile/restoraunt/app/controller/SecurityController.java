package com.mile.pc.mile.restoraunt.app.controller;

import com.mile.pc.mile.restoraunt.app.dto_dao.SignUpDTO;
import com.mile.pc.mile.restoraunt.app.security.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/security")
final class SecurityController {

	private final UserService userService;

  SecurityController(UserService userService) {
    this.userService = userService;
  }

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
