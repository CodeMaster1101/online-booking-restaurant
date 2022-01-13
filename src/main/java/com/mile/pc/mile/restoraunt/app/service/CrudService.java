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

/**
 * This Service class is targeting the restoraunt's admin's functionalities
 * It gives the administrator functionalities such as: adding a new user, deleting the user,
 * adding a promotion to the user, removing the promotion from that user, viewing certain collections, etc...
 * In general, the administrator can access almost every, if not, every end-point in the application.
 * 
 * @author Mile Stanislavov
 *
 */
@Service @Transactional

public class CrudService {
	
	@Autowired UserRepository userRepo;
	@Autowired RoleRepository roleRepo;
	@Autowired CustomTableRepository tableRepo;

	/**
	 * adds a given role to a certain user, throws an exception if the role or user is non existent
	 * @param username
	 * @param roleType
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
	
	/**
	 * removes a role from the user
	 * @param username
	 * @param roleType
	 */
	public void removeRolefromUser(String username, String roleType){
		User user = userRepo.findByUsername(username);
		Role role = roleRepo.findByType(roleType);
		user.getRoles().remove(role);
	}
	
	/**
	 * removes the user found by the given id
	 * @param id
	 */
	public void removeUser(long id) {
		userRepo.deleteById(id);;
	}
	
	/**
	 * removes a certain table based on the table object
	 * @param table
	 */
	public void removeTable(CustomTable table) {
		tableRepo.delete(table);
	}
	
	/**
	 * filters through all the users to fetch every waiter
	 * @return every user that has a role "WAITER"
	 */
	public List<User> getWaiters(){
		return userRepo.findAll().stream()
				.filter(u -> u.getRoles().contains(roleRepo.findByType("WAITER"))).collect(Collectors.toList());
	}
	
	/**
	 * @return every role in the DB
	 */
	public List<Role> allRoles(){
		return roleRepo.findAll();
	}

}
