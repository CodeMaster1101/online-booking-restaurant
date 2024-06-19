package com.mile.pc.mile.restoraunt.app.exceptions;

public final class PasswordException extends RuntimeException{

	public static final String INCORRECT_PASSWORD_TRY_AGAIN = "Incorrect password. Try again";

	public String error() {
		return INCORRECT_PASSWORD_TRY_AGAIN;
	}

}
