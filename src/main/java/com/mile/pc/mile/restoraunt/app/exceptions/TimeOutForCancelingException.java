package com.mile.pc.mile.restoraunt.app.exceptions;


import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;

public final class TimeOutForCancelingException extends RuntimeException{

	private static final String TIMEOUT_FOR_CANCELING = CONSTANTS.CANCEL_TIME + " hours have passed since the moment of your reservation. You can not cancel it, but you can still attend to it.";

	public String error() {
		return TIMEOUT_FOR_CANCELING;
	}

}
