package com.mile.pc.mile.restoraunt.app.comparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import com.mile.pc.mile.restoraunt.app.model.Reservation;

import lombok.Getter;

@Getter
public class TimeComparator implements Comparator<Reservation>{
	
	private boolean goodDistance = true;
	private boolean compareTwoReservationsTimes(Reservation left, Reservation right) {
		
		if(left.getTime().getYear() != right.getTime().getYear())
			return true;
		if(left.getTime().getMonth() != right.getTime().getMonth())
			return true;
		if(left.getTime().getDayOfMonth() != right.getTime().getDayOfMonth()) {
			return true;
		}Set<Double> leftHourContainer = new HashSet<>();
		double leftHourMin = left.getTime().getHour(), leftHourMax = left.getMaxTime().getHour();
		for(double i = leftHourMin; i <= leftHourMax; i++) {
				leftHourContainer.add(i);
		}
		Set<Double> rightHourContainer = new HashSet<>();
		double rightHourMin = right.getTime().getHour(), rightHourMax = right.getMaxTime().getHour();
		for(double i = rightHourMin; i <= rightHourMax; i++) {
				rightHourContainer.add(i);	
		}
		return Collections.disjoint(leftHourContainer, rightHourContainer);
	}
	@Override
	public int compare(Reservation o1, Reservation o2) {
		if(!(compareTwoReservationsTimes(o1, o2))) {
			this.goodDistance = false;
		}return o1.getTime().compareTo(o2.getTime());
	}
}

