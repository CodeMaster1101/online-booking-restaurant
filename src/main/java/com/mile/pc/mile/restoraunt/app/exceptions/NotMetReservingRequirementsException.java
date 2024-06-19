package com.mile.pc.mile.restoraunt.app.exceptions;

public final class NotMetReservingRequirementsException extends RuntimeException {

	private static final String NOT_MET_REQUIREMENTS = "Didn't meet the reservation requirements. You probably have a reservation already. "
			+ "On the other hand, maybe you dont have enough money for the reservation?";

	public String error() {
		return NOT_MET_REQUIREMENTS;
	}

}
