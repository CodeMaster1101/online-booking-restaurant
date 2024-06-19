package com.mile.pc.mile.restoraunt.app.repo;

import com.mile.pc.mile.restoraunt.app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByType(String type);

}
