package com.mile.pc.mile.restoraunt.app;

import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mile.pc.mile.restoraunt.app.model.RestorauntTable;
import com.mile.pc.mile.restoraunt.app.model.Role;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.RoleRepository;
import com.mile.pc.mile.restoraunt.app.repo.TableRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;
import com.mile.pc.mile.restoraunt.app.service.AdminService;

@SpringBootApplication
public class MileRestorauntAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MileRestorauntAppApplication.class, args);
	}
	@Autowired TableRepository tr;
	@Autowired UserRepository ur;
	@Autowired RoleRepository rr;
	@Autowired AdminService cs;
	@Bean
	@Transactional
	CommandLineRunner runner() {
		return args -> {
		
//			if(tr.findAll().size() == 0) {
//				tr.save(new RestorauntTable(null, false, null));
//				tr.save(new RestorauntTable(null, false, null));
//				tr.save(new RestorauntTable(null, false, null));
//				tr.save(new RestorauntTable(null, false, null));
//				tr.save(new RestorauntTable(null, false, null));
//				rr.save(new Role(null, "WAITER"));
//				rr.save(new Role(null, "ADMIN"));
//				rr.save(new Role(null, "USER"));		
//				BCryptPasswordEncoder passwordEncoder1 = new BCryptPasswordEncoder();
//				ur.save(new User(9l,"Mile", "cpthermes", passwordEncoder1.encode("madafaka"), 1500, null, new HashSet<>(), null));
//				cs.AddRoleToUser("cpthermes", "ADMIN");
//				cs.AddRoleToUser("cpthermes", "USER");
				
	//		}	
		};
		
	}
	
}
