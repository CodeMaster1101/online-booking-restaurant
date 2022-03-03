package com.mile.pc.mile.restoraunt.app.service;



import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mile.pc.mile.restoraunt.app.model.Role;
import com.mile.pc.mile.restoraunt.app.model.User;
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
@Service 
public class AdminService {
	
	@Autowired UserRepository userRepo;
	@Autowired RoleRepository roleRepo;
	@Autowired CustomDTOservice dto_Ser;
	/**
	 * adds a given role to a certain user, throws an exception if the role or user is non existent
	 * @param username
	 * @param roleType
	 */
	@SneakyThrows @Transactional
	public void AddRoleToUser(String username, String roleType){
		if(userRepo.getUserByUsername(username)== null || roleRepo.findByType(roleType) == null) {
			throw new Exception("user or role non exsistent");
		}
		User user = userRepo.getUserByUsername(username);
		Role role = roleRepo.findByType(roleType);
		user.getRoles().add(role);
	}
	
	/**
	 * removes a role from the user
	 * @param username
	 * @param roleType
	 */
	@Transactional
	public void removeRolefromUser(String username, String roleType){
		User user = userRepo.getUserByUsername(username);
		Role role = roleRepo.findByType(roleType);
		user.getRoles().remove(role);
	}
	
	/**
	 * removes the user found by the given id
	 * @param id
	 */
	@Transactional
	public void removeUser(long id) {
		userRepo.deleteById(id);;
	}
	
	/**
	 * filters through all the users to fetch every waiter
	 * @return every user that has a role "WAITER"
	 */
	public List<User> getWaiters(){
		return userRepo.findAll().stream()
				.filter(u -> u.getRoles().contains(roleRepo.findByType("WAITER"))).collect(Collectors.toList());
	}

}
