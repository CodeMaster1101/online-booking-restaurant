package com.mile.pc.mile.restoraunt.app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "busy_reservation")
public class BusyReservation {
	@Id @GeneratedValue
	private Long id;
	@OneToOne
	@JsonManagedReference
	private Reservation reservation;
}
