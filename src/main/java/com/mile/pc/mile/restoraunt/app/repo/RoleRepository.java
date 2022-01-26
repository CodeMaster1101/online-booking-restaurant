package com.mile.pc.mile.restoraunt.app.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mile.pc.mile.restoraunt.app.dto.RoleDTO;
import com.mile.pc.mile.restoraunt.app.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByType(String type);
	@Query("SELECT new com.mile.pc.mile.restoraunt.app.dto.admin.RoleDTO(r.type)" +
	"FROM Role r") Set<RoleDTO> roleDTO();
}
