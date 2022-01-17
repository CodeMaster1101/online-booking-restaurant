package com.mile.pc.mile.restoraunt.app.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TableDTO {

	private long id;
	private boolean busy;
	private boolean full;
	private List<ReservationDTO> reservations;
}
