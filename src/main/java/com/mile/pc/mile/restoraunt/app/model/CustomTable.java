package com.mile.pc.mile.restoraunt.app.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomTable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Boolean busy;
    private Boolean full;
    @JsonIgnore
    @OneToMany(mappedBy = "table", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Reservation> reservations;
    
    public void addReservation(Reservation r) {
    	this.reservations.add(r);
    	r.setTable(this);
    }
    public void removeReservation(Reservation r) {
    	r.setTable(null);
    	this.reservations.remove(r);
    }
}

