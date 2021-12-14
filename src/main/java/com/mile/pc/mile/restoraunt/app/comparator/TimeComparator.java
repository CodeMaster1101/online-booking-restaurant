package com.mile.pc.mile.restoraunt.app.comparator;

import java.util.Comparator;

import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;
import com.mile.pc.mile.restoraunt.app.model.Reservation;

import lombok.Getter;

@Getter
public class TimeComparator implements Comparator<Reservation>{

	private boolean goodDistance = true;

	public boolean compareTwoReservationsTimes(Reservation left, Reservation right) {
		if(CONSTANTS.parseLocalTime(left.getTime()).isAfter(CONSTANTS.parseLocalTime(right.getTime()).plusHours(4).minusMinutes(10))
				|| CONSTANTS.parseLocalTime(left.getTime()).isBefore(CONSTANTS.parseLocalTime(right.getTime()).minusHours(4).plusMinutes(10)))
			return true;
		return false;
	}
	@Override
	public int compare(Reservation o1, Reservation o2) {
		if(!(compareTwoReservationsTimes(o1, o2))) {
			this.goodDistance = false;
		}
		return Integer.compare(CONSTANTS.parseLocalTime(o1.getTime()).getHour(), CONSTANTS.parseLocalTime(o2.getTime()).getHour());
	}
}

