package com.mile.pc.mile.restoraunt.app.comparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import com.mile.pc.mile.restoraunt.app.model.Reservation;

import lombok.Getter;
/**
 * 
 * @author Mile Stanislavov
 * 
 * Custom Comparator class, it's goal is to check if one reservation time interval overlaps another.
 * If so, the distance would be bad. Otherwise, it will move on with the algorithm.
 */
@Getter
public class TimeComparator implements Comparator<Reservation>{

	/*
	 * measuring the distance
	 */
	private boolean goodDistance = true;
	/**
	 * Checks if the two reservations are in the same day, if they aren't, then continues. Otherwise,
	 * Compares two Sets of integers, each containing the hours from the beginning to the end of the reservation.
	 * If they have atleast one mutual integer the distance would be bad as the reservations would overlap;
	 * @param left -> x reservation
	 * @param right -> y reservation
	 * @return true if they are not in the same day or they don't have same values in their individual sets.
	 */
	private boolean compareTwoReservationsTimes(Reservation left, Reservation right) {

		if(left.getTime().getYear() != right.getTime().getYear())
			return true;
		if(left.getTime().getMonth() != right.getTime().getMonth())
			return true;
		if(left.getTime().getDayOfMonth() != right.getTime().getDayOfMonth()) 
			return true;
		//creating the sets
		Set<Integer> leftHourContainer = new HashSet<>(), rightHourContainer = new HashSet<>();
		for(int i = left.getTime().getHour(); i <= left.getMaxTime().getHour(); i++) {
			//adds every hour as an integer
			leftHourContainer.add(i);
		}
		for(int i = right.getTime().getHour(); i <= right.getMaxTime().getHour(); i++) {
			rightHourContainer.add(i);	
		}
		//checks if they have mutual elements. return true if they don't
		return Collections.disjoint(leftHourContainer, rightHourContainer);
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

