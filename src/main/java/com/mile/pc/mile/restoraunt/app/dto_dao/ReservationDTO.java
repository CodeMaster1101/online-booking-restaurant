package com.mile.pc.mile.restoraunt.app.dto_dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class ReservationDTO {

	@AssertTrue(message = "Terms must be accepted.")
	private boolean accepted;

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
