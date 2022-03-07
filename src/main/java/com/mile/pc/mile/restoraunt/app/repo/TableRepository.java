package com.mile.pc.mile.restoraunt.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mile.pc.mile.restoraunt.app.model.RestorauntTable;

@Repository
public interface TableRepository extends JpaRepository<RestorauntTable, Long> {
	
}
