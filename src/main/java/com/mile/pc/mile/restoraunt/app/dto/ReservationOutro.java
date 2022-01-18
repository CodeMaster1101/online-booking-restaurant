package com.mile.pc.mile.restoraunt.app.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationOutro {
	private String time;
	private String maxTime;
}
