package com.mile.pc.mile.restoraunt.app.advice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.mile.pc.mile.restoraunt.app.controller.ErrorController;
import com.mile.pc.mile.restoraunt.app.exceptions.AlreadyBusyException;
import com.mile.pc.mile.restoraunt.app.exceptions.BadReservationRadiusException;
import com.mile.pc.mile.restoraunt.app.exceptions.InvalidSpecificTimeException;
import com.mile.pc.mile.restoraunt.app.exceptions.NoAvailableTablesTodayException;
import com.mile.pc.mile.restoraunt.app.exceptions.NotMetReservingRequirementsException;
import com.mile.pc.mile.restoraunt.app.exceptions.PasswordException;
import com.mile.pc.mile.restoraunt.app.exceptions.TimeOutForCancelingException;
import com.mile.pc.mile.restoraunt.app.exceptions.invalidFeeException;

@ControllerAdvice
public class CustomControllerAdvice {

	@Autowired ErrorController controller;
	
	@ExceptionHandler(BindException.class)
	public ModelAndView handleExceptions(BindException e){
		Map<String, String> errors = new HashMap<>();
		
		int i = 1;
		for (FieldError error : e.getFieldErrors()) {
			errors.put("Error " + i + ": ", error.getDefaultMessage());
			i++;
		}
		return controller.errorDisplay(mapToString(errors), HttpStatus.BAD_REQUEST);
	}
	
	private String mapToString(Map<String,String> errors) {
		String[] keys = errors.keySet().toArray(new String[errors.size()]);
		String[] values = errors.values().toArray(new String[errors.size()]);
		String result = "";
		for (int i = 0; i < keys.length; i++) {
			result += keys[i] + values[i] + ", "; 
			}
		return result;
		}
	
	@ExceptionHandler(NoSuchElementException.class)
	public ModelAndView handleExceptions(NoSuchElementException e) {
		return controller.errorDisplay(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(AlreadyBusyException.class)
	public ModelAndView handleExceptions(AlreadyBusyException e) {
		return controller.errorDisplay(e.error(e.getId()), HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(BadReservationRadiusException.class)
	public ModelAndView handleExceptions(BadReservationRadiusException e) {
		return controller.errorDisplay(e.error(e.getTime()), HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(invalidFeeException.class)
	public ModelAndView handleExceptions(invalidFeeException e) {
		return controller.errorDisplay(e.error(), HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(InvalidSpecificTimeException.class)
	public ModelAndView handleExceptions(InvalidSpecificTimeException e) {
		return controller.errorDisplay(e.error(e.getTime(),e.getP()), HttpStatus.BAD_REQUEST);

		
	}
	@ExceptionHandler(NoAvailableTablesTodayException.class)
	public ModelAndView handleExceptions(NoAvailableTablesTodayException e) {
		return controller.errorDisplay(e.error(e.getDate()), HttpStatus.CONFLICT);
		
	}
	@ExceptionHandler(NotMetReservingRequirementsException.class)
	public ModelAndView handleExceptions(NotMetReservingRequirementsException e) {
		return controller.errorDisplay(e.error(), HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(PasswordException.class)
	public ModelAndView handleExceptions(PasswordException e) {
		return controller.errorDisplay(e.error(), HttpStatus.CONFLICT);
		
	}
	@ExceptionHandler(TimeOutForCancelingException.class)
	public ModelAndView handleExceptions(TimeOutForCancelingException e) {
		return controller.errorDisplay(e.error(), HttpStatus.REQUEST_TIMEOUT);
	}
}
