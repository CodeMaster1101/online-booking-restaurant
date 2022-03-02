package com.mile.pc.mile.restoraunt.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class mainPageController {

	@Autowired UserController controller;
	@GetMapping(path = {"/", ""})
	public ModelAndView mainPage() {
		return controller.homePage();
	}
}
