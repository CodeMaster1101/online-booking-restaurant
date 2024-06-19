package com.mile.pc.mile.restoraunt.app.dto_dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDAO {
	
	private long id;

	private String firstName;

	private String username;

	private long balance;

	private Set<String> roles;
	
}
