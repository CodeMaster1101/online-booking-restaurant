package com.mile.pc.mile.restoraunt.app.repo;

import com.mile.pc.mile.restoraunt.app.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {}
