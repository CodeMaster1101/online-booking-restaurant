package com.mile.pc.mile.restoraunt.app;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.RoleRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;
import com.mile.pc.mile.restoraunt.app.service.AdminService;

@SpringBootApplication
public class MileRestorauntAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MileRestorauntAppApplication.class, args);
	}
	
	@Autowired CustomTableRepository tr;
	@Autowired UserRepository ur;
	@Autowired ReservationRepository reser;
	@Autowired RoleRepository rr;
	@Autowired AdminService cs;

	@Bean
	@Transactional
	CommandLineRunner commandLineRunner() {
		return args -> {
//			tr.save(new CustomTable(null, false, null));
//			tr.save(new CustomTable(null, false, null));
//			tr.save(new CustomTable(null, false, null));
//			tr.save(new CustomTable(null, false, null));
//			tr.save(new CustomTable(null, false, null));
//			rr.save(new Role(null, "WAITER"));
//			rr.save(new Role(null, "ADMIN"));
//			rr.save(new Role(null, "USER"));		
//			BCryptPasswordEncoder passwordEncoder1 = new BCryptPasswordEncoder();
//			ur.save(new User(9l,"Mile", "cpthermes", passwordEncoder1.encode("madafaka"), 1500, null, new HashSet<>(), null));
//			ur.save(new User(null,"Joco", "user2", passwordEncoder1.encode("wow"), 1500, null, new HashSet<>(), null));
//			ur.save(new User(null, "Mario", "user3", "123", 1500, null, new HashSet<>(), null));
//			ur.save(new User(null, "Stevo", "user4", "123", 1500, null, new HashSet<>(), null));
//			
//			cs.AddRoleToUser("cpthermes", "ADMIN");
//			cs.AddRoleToUser("cpthermes", "USER");
//			cs.AddRoleToUser("user2", "USER");
			//cs.AddRoleToUser("cpthermes", "WAITER");
		};
	}
	
}
