package com.mile.pc.mile.restoraunt.app.dto;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

	private LocalDateTime time;
	private LocalDateTime maxTime;
}
