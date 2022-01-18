package com.mile.pc.mile.restoraunt.app.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Component;

import com.mile.pc.mile.restoraunt.app.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

	private boolean accepted;
	private User user;
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime time;
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime maxTime;
	private long tableid;
}
