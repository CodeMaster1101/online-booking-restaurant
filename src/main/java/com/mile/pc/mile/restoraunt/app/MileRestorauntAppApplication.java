package com.mile.pc.mile.restoraunt.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MileRestorauntAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MileRestorauntAppApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {

		};
	}
	
}
