package com.mile.pc.mile.restoraunt.app.exceptions;

import java.time.LocalDate;

public final class NoAvailableTablesTodayException extends RuntimeException {

	private final LocalDate date;

  public NoAvailableTablesTodayException(LocalDate date) {
    this.date = date;
  }

  public String error() {
		return "We are sorry, but there are no available tables at this period for " + date;
	}
}
