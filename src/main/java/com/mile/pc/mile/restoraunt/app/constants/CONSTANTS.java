package com.mile.pc.mile.restoraunt.app.constants;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Component
public class CONSTANTS {

	public final static long  BREAKFAST_FEE = 150;
	public final static long  LUNCH_FEE = 300;
	public final static long  DINNER_FEE = 600;
	public final static int AFTER_RESERVATION_TIME = 50;
	public final static int BEFORE_RESERVATION_TIME = 50;
	public static final LocalTime START = LocalTime.of(7, 0);
	public static final LocalTime END = LocalTime.of(22, 00);
	public static final LocalTime NOON = LocalTime.of(12, 0);
	public static final LocalTime EVENING = LocalTime.of(20, 0);
	public static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm";
    public static final LocalDateTime FIXED_DATE = LocalDateTime.now();
    public static final LocalTime MAX_TIME= LocalTime.of(23, 59);
	public static final int CANCEL_TIME = 2;
	public static final long MINIMUM_DAY = 1; 
}
