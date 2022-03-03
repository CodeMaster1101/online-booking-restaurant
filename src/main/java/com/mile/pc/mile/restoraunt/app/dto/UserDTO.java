package com.mile.pc.mile.restoraunt.app.dto;

import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
	private long id;
	private String firstName;
	private String username;
	private long balance;
	private Set<String> roles;
	
	public UserDTO(long id, String username, long balance) {
		this.id = id;
		this.username = username;
		this.balance = balance;
	}
}
