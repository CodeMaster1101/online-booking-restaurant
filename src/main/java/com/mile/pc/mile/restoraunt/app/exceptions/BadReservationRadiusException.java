package com.mile.pc.mile.restoraunt.app.exceptions;

import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;

import java.time.LocalDateTime;

public final class BadReservationRadiusException extends RuntimeException {

	private final LocalDateTime time;

  public BadReservationRadiusException(LocalDateTime time) {
    this.time = time;
  }

  public String error() {
		return "Invalid date. The minimum time for the reservation must be a day from now. For the time of your reservation, which is: "
				+ time.toString() + " The minimal time would be " + time.plusDays(CONSTANTS.MINIMUM_DAYS);
	}

}
