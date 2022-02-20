package com.mile.pc.mile.restoraunt.app.exceptions;

import org.springframework.stereotype.Component;

import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;

@Component
public class TimeOutForCancelingException extends Exception{
    
	private static final long serialVersionUID = 6223439866695078278L;

	public String error() {
		int afterMoment = CONSTANTS.CANCEL_TIME;
		return "" + afterMoment + " hours have passed since the moment of your reservation. You can not cancel it, but you can still attend to it.";
	}
}
