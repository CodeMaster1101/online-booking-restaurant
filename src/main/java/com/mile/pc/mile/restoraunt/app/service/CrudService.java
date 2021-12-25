package com.mile.pc.mile.restoraunt.app.service;



import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mile.pc.mile.restoraunt.app.model.CustomTable;
import com.mile.pc.mile.restoraunt.app.model.Role;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.RoleRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

import lombok.SneakyThrows;

@Service @Transactional
public class CrudService {
	@Autowired UserRepository userRepo;
	@Autowired RoleRepository roleRepo;
	@Autowired CustomTableRepository tableRepo;

	/*
	 * Basic ADMIN Functions
	 */
	/*
	 * Role based
	 */
	@SneakyThrows
	public void AddRoleToUser(String username, String roleType){
		if(userRepo.findByUsername(username)== null || roleRepo.findByType(roleType) == null) {
			throw new Exception("user or role non exsistent");
		}
		User user = userRepo.findByUsername(username);
		Role role = roleRepo.findByType(roleType);
		System.out.println();
		user.getRoles().add(role);
	}
	public void removeRolefromUser(String username, String roleType){
		User user = userRepo.findByUsername(username);
		Role role = roleRepo.findByType(roleType);
		user.getRoles().remove(role);
	}
	public void removeUser(long id) {
		userRepo.deleteById(id);;
	}
	public void removeTable(CustomTable table) {
		tableRepo.delete(table);
	}
	public User addUser(User user) {
		return userRepo.save(user);
	}
	public CustomTable addTable(CustomTable table) {
		return tableRepo.save(table);
	}
	public List<User> getWaiters(){
		return userRepo.findAll().stream()
				.filter(u -> u.getRoles().contains(roleRepo.findByType("WAITER"))).collect(Collectors.toList());
	}
	public List<Role> allRoles(){
		return roleRepo.findAll();
	}
	public Collection<Role> userRoles(long id){
		User user = userRepo.findById(id).get();
		return user.getRoles();
	}

}
