package com.mile.pc.mile.restoraunt.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public final class ErrorController {

	public ModelAndView errorDisplay(String message, HttpStatus status) {
		ModelAndView m = new ModelAndView("error-page");
		m.addObject("error", message);
		m.addObject("status", status.toString());
		return m;
	}

}
