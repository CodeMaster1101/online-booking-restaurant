package com.mile.pc.mile.restoraunt.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mile.pc.mile.restoraunt.app.model.BusyReservation;
import com.mile.pc.mile.restoraunt.app.model.Reservation;

@Repository
public interface CurrentDeployedReservations extends JpaRepository<BusyReservation, Long>{
   BusyReservation findByReservation(Reservation reservation);
}
