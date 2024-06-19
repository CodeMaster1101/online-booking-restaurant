package com.mile.pc.mile.restoraunt.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
final class MainPageController {

	@GetMapping(path = {"", "/home"})
	public ModelAndView homePage() {
		return new ModelAndView("index");
	}

}
