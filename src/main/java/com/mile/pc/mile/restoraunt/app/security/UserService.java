package com.mile.pc.mile.restoraunt.app.security;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.mile.pc.mile.restoraunt.app.dto_dao.SignUpDTO;

public interface UserService extends UserDetailsService {
	
	void save(SignUpDTO registrationDto);

}
