package com.mile.pc.mile.restoraunt.app.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mile.pc.mile.restoraunt.app.model.Reservation;
import com.mile.pc.mile.restoraunt.app.model.User;

@Transactional
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	Reservation findByUser(User user);
}
