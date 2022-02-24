package com.mile.pc.mile.restoraunt.app.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mile.pc.mile.restoraunt.app.dto.SignUpDTO;
import com.mile.pc.mile.restoraunt.app.model.Role;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.RoleRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired UserRepository userRepository;
	@Autowired RoleRepository roleRepository;
	@Autowired BCryptPasswordEncoder passwordEncoder;
	
	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	@Override @Transactional
	public User save(SignUpDTO registrationDto) {
		Set<Role> roles = new HashSet<>();
		Role role_user = roleRepository.findByType("USER");
		roles.add(role_user);
		User user = new User(null, registrationDto.getFirstName(), registrationDto.getUsername(), 
				passwordEncoder.encode(registrationDto.getPassword()), 1500, null, roles, null);
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		User user = userRepository.getUserByUsername(username);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));		
	}
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getType())).collect(Collectors.toList());
	}
}
