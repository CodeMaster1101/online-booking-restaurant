package com.mile.pc.mile.restoraunt.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mile.pc.mile.restoraunt.app.model.LivingReservation;
import com.mile.pc.mile.restoraunt.app.model.Reservation;

@Repository
public interface CurrentDeployedReservations extends JpaRepository<LivingReservation, Long>{
   LivingReservation findByReservation(Reservation reservation);
}
