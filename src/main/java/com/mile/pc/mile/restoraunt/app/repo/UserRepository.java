package com.mile.pc.mile.restoraunt.app.repo;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mile.pc.mile.restoraunt.app.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Query(
			value = "SELECT * FROM USERS u WHERE u.reservationMoment = null ", 
			nativeQuery = true)
	Collection<User> findAllUsersWithoutReservation();
	@Query("SELECT u FROM User u WHERE u.username = :username")
	public User getUserByUsername(@Param("username") String username);	
}
