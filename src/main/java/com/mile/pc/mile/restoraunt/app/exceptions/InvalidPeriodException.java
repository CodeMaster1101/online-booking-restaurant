package com.mile.pc.mile.restoraunt.app.exceptions;

public final class InvalidPeriodException extends RuntimeException {

	private static final String INVALID_PERIOD = "Invalid period";

	public String error() {
		return INVALID_PERIOD;
	}

}
