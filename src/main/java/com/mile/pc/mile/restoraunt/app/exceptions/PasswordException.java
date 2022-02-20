package com.mile.pc.mile.restoraunt.app.exceptions;

import org.springframework.stereotype.Component;


@Component
public class PasswordException extends Exception{

	private static final long serialVersionUID = -5213902381068675725L;
	
	public String error() {
		return "Incorrect password. Try again";
	}
}
