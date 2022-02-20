package com.mile.pc.mile.restoraunt.app.exceptions;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class AlreadyBusyException extends Exception {

	private static final long serialVersionUID = -8812306212592169302L;
	
	private long id;
	
	public String error(long id) {
	return "The table with ID: " + id + " is already occupied";	
	}

}
