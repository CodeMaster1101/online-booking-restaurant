package com.mile.pc.mile.restoraunt.app.advice;

import com.mile.pc.mile.restoraunt.app.controller.ErrorController;
import com.mile.pc.mile.restoraunt.app.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
final class CustomControllerAdvice {

	private final ErrorController controller;

  CustomControllerAdvice(ErrorController controller) {
    this.controller = controller;
  }

  @ExceptionHandler(BindException.class)
	ModelAndView handleExceptions(BindException e) {
		Map<String, String> errors = new HashMap<>();

		int i = 1;
		for (FieldError error : e.getFieldErrors()) {
			errors.put("Error " + i + ": ", error.getDefaultMessage());
			i++;
		}
		return controller.errorDisplay(mapToString(errors), HttpStatus.BAD_REQUEST);
	}

	private String mapToString(Map<String,String> errors) {
		String[] keys = errors.keySet().toArray(new String[0]);
		String[] values = errors.values().toArray(new String[0]);
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < keys.length; i++) {
			result.append(keys[i]).append(values[i]).append(" ");
		}
		return result.toString();
	}
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	ModelAndView handleExceptions() {
		return controller.errorDisplay("username already taken", HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(NoSuchElementException.class)
	ModelAndView handleExceptions(NoSuchElementException e) {
		return controller.errorDisplay(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AlreadyBusyException.class)
	ModelAndView handleExceptions(AlreadyBusyException e) {
		return controller.errorDisplay(e.error(), HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(BadReservationRadiusException.class)
	ModelAndView handleExceptions(BadReservationRadiusException e) {
		return controller.errorDisplay(e.error(), HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(InvalidPeriodException.class)
	ModelAndView handleExceptions(InvalidPeriodException e) {
		return controller.errorDisplay(e.error(), HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(InvalidSpecificTimeException.class)
	ModelAndView handleExceptions(InvalidSpecificTimeException e) {
		return controller.errorDisplay(e.error(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NoAvailableTablesTodayException.class)
	ModelAndView handleExceptions(NoAvailableTablesTodayException e) {
		return controller.errorDisplay(e.error(), HttpStatus.CONFLICT);

	}
	
	@ExceptionHandler(NotMetReservingRequirementsException.class)
	ModelAndView handleExceptions(NotMetReservingRequirementsException e) {
		return controller.errorDisplay(e.error(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(PasswordException.class)
	ModelAndView handleExceptions(PasswordException e) {
		return controller.errorDisplay(e.error(), HttpStatus.CONFLICT);

	}
	
	@ExceptionHandler(TimeOutForCancelingException.class)
	ModelAndView handleExceptions(TimeOutForCancelingException e) {
		return controller.errorDisplay(e.error(), HttpStatus.REQUEST_TIMEOUT);
	}
	
}
