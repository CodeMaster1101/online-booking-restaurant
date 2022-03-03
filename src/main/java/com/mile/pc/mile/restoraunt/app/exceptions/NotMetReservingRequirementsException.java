package com.mile.pc.mile.restoraunt.app.exceptions;

import org.springframework.stereotype.Component;

@Component

public class NotMetReservingRequirementsException extends RuntimeException {

	private static final long serialVersionUID = 7407325878833614802L;
	
	public String error() {
		return "Didn't meet the reservation requirements. You probably have a reseration already. "
				+ "On the other hand, maybe you dont have enough money for the reservation?";
	}
}
