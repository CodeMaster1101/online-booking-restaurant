package com.mile.pc.mile.restoraunt.app;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.mile.pc.mile.restoraunt.app.model.CustomTable;
import com.mile.pc.mile.restoraunt.app.model.Role;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.RoleRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;
import com.mile.pc.mile.restoraunt.app.service.CrudService;

@SpringBootApplication
public class MileRestorauntAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MileRestorauntAppApplication.class, args);
	}
	@Autowired CustomTableRepository tr;
	@Autowired UserRepository ur;
	@Autowired ReservationRepository reser;
	@Autowired RoleRepository rr;
	@Autowired CrudService cs;
	@Bean
	@Transactional
	CommandLineRunner commandLineRunner() {
		return args -> {
			tr.save(new CustomTable(null, false, false, null));
			tr.save(new CustomTable(null, false, false, null));
			tr.save(new CustomTable(null, false, false, null));
			tr.save(new CustomTable(null, false, false, null));
			tr.save(new CustomTable(null, false, false, null));

			rr.save(new Role(null, "WAITER"));
			rr.save(new Role(null, "ADMIN"));
			rr.save(new Role(null, "USER"));
//
//			ur.save(new User(null, "cpthermes", "madafaka", 123987456l, null, new HashSet<>(), null));
//			ur.save(new User(null, "user2", "wow", 123987456l, null, new HashSet<>(), null));
//			ur.save(new User(null, "user3", "123", 1231231l, null, new HashSet<>(), null));
//			ur.save(new User(null, "user4", "123", 123987456l, null, new HashSet<>(), null));
//			ur.save(new User(null, "user5", "123", 123987456l, null, new HashSet<>(), null));
//			ur.save(new User(null, "user6", "123", 123987456l, null, new HashSet<>(), null));

			
			//cs.AddRoleToUser("cpthermes", "WAITER");
		};
	}
	
}
