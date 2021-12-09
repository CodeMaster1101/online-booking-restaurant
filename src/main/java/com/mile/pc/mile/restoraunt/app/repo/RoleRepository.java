package com.mile.pc.mile.restoraunt.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mile.pc.mile.restoraunt.app.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByType(String type);
}
