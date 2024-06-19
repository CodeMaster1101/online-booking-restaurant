package com.mile.pc.mile.restoraunt.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	private Boolean accepted;

	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonManagedReference
	private User user;

	@ManyToOne
	@JsonBackReference
	private RestaurantTable table;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime time;

	private Long fee;

	private boolean busy;

	private int period;

	private String note;

	public void setUTable(RestaurantTable table) {
		this.table = table;
		table.addReservation(this);
	}

}
