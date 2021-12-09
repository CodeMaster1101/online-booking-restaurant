package com.mile.pc.mile.restoraunt.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mile.pc.mile.restoraunt.app.model.CustomTable;
import com.mile.pc.mile.restoraunt.app.model.Reservation;

@Repository
public interface CustomTableRepository extends JpaRepository<CustomTable, Long>{
	List<CustomTable> findByAvailable(Boolean available);
	CustomTable findByReservation(Reservation reservation);
}
