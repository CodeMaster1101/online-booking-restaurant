package com.mile.pc.mile.restoraunt.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mile.pc.mile.restoraunt.app.model.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long>{

}
