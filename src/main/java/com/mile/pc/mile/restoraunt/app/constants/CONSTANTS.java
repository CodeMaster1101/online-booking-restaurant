package com.mile.pc.mile.restoraunt.app.constants;

import java.time.LocalTime;

public final class CONSTANTS {

  private CONSTANTS() {
		throw new IllegalStateException("Can not make instances of this class");
	}

	public static final long  BREAKFAST_FEE = 150;
	public static final long  LUNCH_FEE = 300;
	public static final long  DINNER_FEE = 600;
	public static final int AFTER_RESERVATION_TIME = 50;
	public static final int BEFORE_RESERVATION_TIME = 50;
	public static final LocalTime START = LocalTime.of(7, 0);
	public static final LocalTime END = LocalTime.of(22, 0);
	public static final LocalTime NOON = LocalTime.of(12, 0);
	public static final LocalTime EVENING = LocalTime.of(20, 0);
	public static final int CANCEL_TIME = 2;
	public static final long MINIMUM_DAYS = 1;

}
