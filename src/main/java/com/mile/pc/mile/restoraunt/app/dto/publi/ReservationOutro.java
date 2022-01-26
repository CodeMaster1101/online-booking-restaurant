package com.mile.pc.mile.restoraunt.app.dto.publi;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ReservationOutro {
	private String time;
	private String maxTime;
}
