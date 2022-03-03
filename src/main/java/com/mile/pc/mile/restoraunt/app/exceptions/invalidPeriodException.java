package com.mile.pc.mile.restoraunt.app.exceptions;

import org.springframework.stereotype.Component;

@Component

public class invalidPeriodException extends RuntimeException {

	private static final long serialVersionUID = 8673623185353316909L;
	public String error() {
		return "Invalid period.";
	}

}
