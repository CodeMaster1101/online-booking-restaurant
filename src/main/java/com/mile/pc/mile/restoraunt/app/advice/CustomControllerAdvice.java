package com.mile.pc.mile.restoraunt.app.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

	@ExceptionHandler(BindException.class)
	public ResponseEntity<Map<String, String>> handleExceptions(BindException e){
		Map<String, String> errors = new HashMap<>();
		
		int i = 1;
		for (FieldError error : e.getFieldErrors()) {
			errors.put("Error " + i + ": ", error.getDefaultMessage());
			i++;
		}
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(AlreadyBusyException.class)
	public ResponseEntity<String> handleExceptions(AlreadyBusyException e) {
		return new ResponseEntity<String>(e.error(e.getId()), HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(BadReservationRadiusException.class)
	public ResponseEntity<String> handleExceptions(BadReservationRadiusException e) {
		return new ResponseEntity<String>(e.error(e.getTime()), HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(invalidFeeException.class)
	public ResponseEntity<String> handleExceptions(invalidFeeException e) {
		return new ResponseEntity<String>(e.error(), HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(InvalidSpecificTimeException.class)
	public ResponseEntity<String> handleExceptions(InvalidSpecificTimeException e) {
		return new ResponseEntity<String>(e.error(e.getTime(), e.getP()), HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(NoAvailableTablesTodayException.class)
	public ResponseEntity<String> handleExceptions(NoAvailableTablesTodayException e) {
		return new ResponseEntity<String>(e.error(e.getDate()), HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(NotMetReservingRequirementsException.class)
	public ResponseEntity<String> handleExceptions(NotMetReservingRequirementsException e) {
		return new ResponseEntity<String>(e.error(), HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(PasswordException.class)
	public ResponseEntity<String> handleExceptions(PasswordException e) {
		return new ResponseEntity<String>(e.error(), HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(TimeOutForCancelingException.class)
	public ResponseEntity<String> handleExceptions(TimeOutForCancelingException e) {
		return new ResponseEntity<String>(e.error(), HttpStatus.BAD_REQUEST);
	}
}
