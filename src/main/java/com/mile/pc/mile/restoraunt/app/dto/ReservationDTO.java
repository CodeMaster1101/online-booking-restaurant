package com.mile.pc.mile.restoraunt.app.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

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

	@AssertTrue(message = "Terms must be accepted.")
	private boolean accepted;
	private String username;
	@NotNull(message = "Select a date")
	@DateTimeFormat( iso = ISO.DATE)
	private LocalDate date;
	@NotNull(message = "Select a period")
	private int period;
	@DateTimeFormat(iso = ISO.TIME)
	@NotNull(message = "Please, specify a time!")
	private LocalTime time;
	private String note;
}
