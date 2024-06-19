package com.mile.pc.mile.restoraunt.app.dto_dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class ReservationWaiterDAO {
	
	private long tableId;

	private String firstName;

	private String username;

	private String period;

	@DateTimeFormat(iso = ISO.TIME)
	private LocalTime time;

	private boolean arrived;

	private String note;

}
