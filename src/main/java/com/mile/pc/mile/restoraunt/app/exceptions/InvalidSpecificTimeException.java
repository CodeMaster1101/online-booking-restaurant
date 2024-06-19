package com.mile.pc.mile.restoraunt.app.exceptions;

public final class InvalidSpecificTimeException extends RuntimeException {

	private final int period;

	public InvalidSpecificTimeException(int period) {
		this.period = period;
	}

	public String error() {
		return "The specified time is not within the selected period " + " '" + periodToString(period) + "'";
	}

	private String periodToString(int p) {
		switch (p) {
		case 1:return "Breakfast";
		case 2:return "Lunch";
		case 3:return "Dinner";
		default:
			return "unidentified";
		}
	}
}
