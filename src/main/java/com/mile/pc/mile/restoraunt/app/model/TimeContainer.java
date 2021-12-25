package com.mile.pc.mile.restoraunt.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TimeContainer {
	@Id
	@GeneratedValue
	private Long id;
	private LocalDateTime time;
	@OneToOne
	@JsonManagedReference
	private User user;
}
