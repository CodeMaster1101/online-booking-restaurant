package com.mile.pc.mile.restoraunt.app.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

	private boolean accepted;
	private String username;
	private String password;
	@DateTimeFormat( iso = ISO.DATE)
	private LocalDate date;
	private int period;
	@DateTimeFormat(iso = ISO.TIME)
	private LocalTime time;
	private String note;
}
