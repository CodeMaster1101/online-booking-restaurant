package com.mile.pc.mile.restoraunt.app.exceptions;

public final class AlreadyBusyException extends RuntimeException {

	private final long id;

  public AlreadyBusyException(long id) {
    this.id = id;
  }

  public String error() {
		return "The table with ID: " + id + " is already occupied";	

	}

}
