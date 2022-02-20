package com.mile.pc.mile.restoraunt.app.exceptions;

import java.time.LocalTime;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class InvalidSpecificTimeException extends Exception {

	private LocalTime time;
	private int p;
	private static final long serialVersionUID = -4095642602516033323L;
	
	public String error(LocalTime time, int p) {
		return "The specified time is not within the selected period " + " '" +periodToString(p)+"'";
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
