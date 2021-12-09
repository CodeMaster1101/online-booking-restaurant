package com.mile.pc.mile.restoraunt.app.constants;

import java.time.LocalTime;

import org.springframework.stereotype.Component;

@Component
public class CONSTANTS {
	public final static int  fee = 100;
	public static LocalTime parseLocalTime(String value) {
		 return LocalTime.parse(value);
		}
}
