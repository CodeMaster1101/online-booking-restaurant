package com.mile.pc.mile.restoraunt.app.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private CustomTable table;
    private LocalDateTime time;
    private LocalDateTime maxTime;
    private int numberOfPeople;
    @JsonIgnore
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private BusyReservation livingReservation;
    @OneToOne(orphanRemoval = true)
    private Guest guest;
    private Long fee;
    private boolean expired;
    public void setUTable(CustomTable table) {
    	this.table = table;
    	table.addReservation(this);
    }
  
}

