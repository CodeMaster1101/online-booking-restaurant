package com.mile.pc.mile.restoraunt.app.comparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import com.mile.pc.mile.restoraunt.app.model.Reservation;

import lombok.Getter;

@Getter
public class TimeComparator implements Comparator<Reservation>{

	/*
	 * measuring the distance
	 */
	private boolean goodDistance = true;
	
	/**
	 * @param left
	 * @param right
	 * @return true if the two reservation left and right are not in the same day.
	 */
	private boolean checkNotSameDay(Reservation left, Reservation right) {
		if(left.getTime().getYear() != right.getTime().getYear())
			return true;
		if(left.getTime().getMonth() != right.getTime().getMonth())
			return true;
		if(left.getTime().getDayOfMonth() != right.getTime().getDayOfMonth()) 
			return true;
		return false;
	}
	private boolean compareTwoReservationsTimes(Reservation o1, Reservation o2) {
		if(checkNotSameDay(o1, o2) || (o1.getFee() != o2.getFee())) 
			return true;
		return false;
	}
	/**
	 * simple comparing method in which, for every loop the two reservations o1 and o2 would be compared
	 * as well as their time intervals mentioned above.
	 */
	@Override
	public int compare(Reservation o1, Reservation o2) {
		if(!(compareTwoReservationsTimes(o1, o2))) {
			this.goodDistance = false;
		}return o1.getTime().compareTo(o2.getTime());
	}
}

