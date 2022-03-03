package com.mile.pc.mile.restoraunt.app.exceptions;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class BadReservationRadiusException extends RuntimeException {

	private LocalDateTime time;
	private static final long serialVersionUID = -7303926283853494424L;
	public String error(LocalDateTime rTime) {
		return "Invalid date. The minimum time for the reservation must be a day from now. For the time of your reservation, which is: "
				+ rTime.toString() + " The minimal time would be " + rTime.plusDays(CONSTANTS.MINIMUM_DAY);
	}
}
