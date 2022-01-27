package com.mile.pc.mile.restoraunt.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mile.pc.mile.restoraunt.app.model.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long>{
//	@Query("SELECT new com.mile.pc.mile.restoraunt.app.dto.ReservationOutro(r.time, r.maxTime) "
//			+ "FROM Reservation r WHERE r.guest.id = ?#{[0]}") List<ReservationOutro> guestReservations(long id);
	
//	@Query("SELECT new com.mile.pc.mile.restoraunt.app.dto.ReservationOutro(r.time, r.maxTime) "
//			+ "FROM Reservation r WHERE r.guest.id = :{#guest.id}") List<ReservationOutro> guestReservations(@Param("guest") Guest guest);
}
