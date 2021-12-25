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
		if(left.getTime().getDayOfMonth() != right.getTime().getDayOfMonth()) {
			return true;
		}Set<Integer> leftHourContainer = new HashSet<>();
		Integer leftHourMin = left.getTime().getHour(), leftHourMax = left.getMaxTime().getHour();
		for(int i = leftHourMin; i <= leftHourMax; i++) {
			leftHourContainer.add(i);
		}Set<Integer> rightHourContainer = new HashSet<>();
		Integer rightHourMin = left.getTime().getHour(), rightHourMax = left.getMaxTime().getHour();
		for(int i = rightHourMin; i <= rightHourMax; i++) {
			rightHourContainer.add(i);
		}
		return  Collections.disjoint(leftHourContainer, rightHourContainer);
	}
	@Override
	public int compare(Reservation o1, Reservation o2) {
		
		if(!(compareTwoReservationsTimes(o1, o2))) {
			this.goodDistance = false;
		}
		return o1.getTime().compareTo(o2.getTime());
	}
}

