package com.mile.pc.mile.restoraunt.app.security;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.mile.pc.mile.restoraunt.app.dto_dao.SignUpDTO;
import com.mile.pc.mile.restoraunt.app.model.User;

public interface UserService extends UserDetailsService {
	
	User save(SignUpDTO registrationDto);
}
