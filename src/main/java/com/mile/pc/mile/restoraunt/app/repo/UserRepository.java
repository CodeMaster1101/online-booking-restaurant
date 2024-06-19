package com.mile.pc.mile.restoraunt.app.repo;

import com.mile.pc.mile.restoraunt.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u.username FROM User u WHERE u.id = ?1")
	Optional<String> getUsernameById(long id);

	User findByUsername(String username);

}
