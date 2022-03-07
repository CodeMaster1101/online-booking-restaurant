package com.mile.pc.mile.restoraunt.app.dto_dao;

import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDAO {
	
	private long id;
	private String firstName;
	private String username;
	private long balance;
	private Set<String> roles;
	
}
