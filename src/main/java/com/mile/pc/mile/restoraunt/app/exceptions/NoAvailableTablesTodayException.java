package com.mile.pc.mile.restoraunt.app.exceptions;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Component
public class NoAvailableTablesTodayException extends RuntimeException {
	
	private LocalDate date;
	private static final long serialVersionUID = 2475694739398696095L;
	public String error(LocalDate date) {
		return "We are sorry, but there are no available tables at this period for " + date;
	}
}
