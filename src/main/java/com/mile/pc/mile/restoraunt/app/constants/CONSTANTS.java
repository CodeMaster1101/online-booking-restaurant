package com.mile.pc.mile.restoraunt.app.constants;

import java.time.LocalTime;

import org.springframework.stereotype.Component;

@Component
public class CONSTANTS {
	public final static int  fee = 150;
	public final static int afterReservationTime = 40;
	public static final LocalTime START = LocalTime.of(7, 0);
	public static final LocalTime END = LocalTime.of(22, 0);


}
