package com.mile.pc.mile.restoraunt.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation
{
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Boolean accepted;
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;
    @ManyToOne
    @JoinColumn(name = "table_id")
    @JsonBackReference
    private RestorauntTable table;
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime time;
    private Long fee;
    private boolean busy;
    private int period;
    private String note;
    public void setUTable(RestorauntTable table) {
    	this.table = table;
    	table.addReservation(this);
    }
  
}