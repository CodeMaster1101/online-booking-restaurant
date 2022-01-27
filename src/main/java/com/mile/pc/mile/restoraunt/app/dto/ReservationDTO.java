package com.mile.pc.mile.restoraunt.app.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

	private boolean accepted;
	private String username;
	private String password;
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime time;
	@DateTimeFormat(iso = ISO.TIME)
	private LocalTime maxTime;
	private long tableid;
	
	public ReservationDTO(LocalDateTime time, LocalTime maxTime) {
		this.time = time;
		this.maxTime = maxTime;
	}
}
