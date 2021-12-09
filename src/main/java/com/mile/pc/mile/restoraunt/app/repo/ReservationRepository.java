package com.mile.pc.mile.restoraunt.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.User;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	Reservation findByUser(User user);
}
