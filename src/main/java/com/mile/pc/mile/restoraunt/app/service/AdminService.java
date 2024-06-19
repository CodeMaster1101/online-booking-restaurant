package com.mile.pc.mile.restoraunt.app.service;

import com.mile.pc.mile.restoraunt.app.model.Role;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.RoleRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Administrative section corresponds to the manager/CEO functionalities of the app.
 * The manager has the following abilities/functionalities:
 * <p>
 * - Manage all users (promote a certain user to a waiter/admin, demote a user to an ordinary client)
 * - View all roles. The roles of a user determine whether they are authorized or not for a certain URL.
 * - Remove a user.
 * <p>
 * This class provides methods to perform these administrative tasks.
 *
 * @see User
 * @see Role
 * @see UserRepository
 * @see RoleRepository
 *
 * @autor Mile Stanislavov
 */
@Service
public class AdminService {

	private final UserRepository userRepo;
	private final RoleRepository roleRepo;

	/**
	 * Constructor to initialize the UserRepository and RoleRepository.
	 *
	 * @param userRepo the repository to manage User entities
	 * @param roleRepo the repository to manage Role entities
	 */
	public AdminService(UserRepository userRepo, RoleRepository roleRepo) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
	}

	/**
	 * Adds a role to a user.
	 *
	 * @param username the username of the user
	 * @param roleType the type of the role to be added
	 * @throws EntityNotFoundException if the user or role does not exist
	 */
	@SneakyThrows @Transactional
	public void addRoleToUser(String username, String roleType) {
		User user = userRepo.findByUsername(username);
		Role role = roleRepo.findByType(roleType);
		validateUserAndRoleReferences(user, role);
		user.getRoles().add(role);
	}

	/**
	 * Removes a role from a user.
	 *
	 * @param username the username of the user
	 * @param roleType the type of the role to be removed
	 * @throws EntityNotFoundException if the user or role does not exist
	 */
	@Transactional
	public void removeRoleFromUser(String username, String roleType) {
		User user = userRepo.findByUsername(username);
		Role role = roleRepo.findByType(roleType);
		validateUserAndRoleReferences(user, role);
		user.getRoles().remove(role);
	}

	/**
	 * Removes a user by their ID.
	 *
	 * @param id the ID of the user to be removed
	 */
	@Transactional
	public void removeUser(long id) {
		userRepo.deleteById(id);
	}

	/**
	 * Retrieves all users with the 'WAITER' role.
	 *
	 * @return a list of users with the 'WAITER' role
	 */
	public List<User> getWaiters() {
		return userRepo.findAll().stream()
				.filter(u -> u.getRoles().contains(roleRepo.findByType("WAITER"))).collect(Collectors.toList());
	}

	/**
	 * Retrieves the username of a user by their ID.
	 *
	 * @param id the ID of the user
	 * @return the username of the user
	 * @throws EntityNotFoundException if the user is not found
	 */
	public String getUsernameById(long id) {
		return userRepo.getUsernameById(id).orElseThrow(() -> new EntityNotFoundException("User not found by id: " + id));
	}

	/**
	 * Retrieves a user by their ID.
	 *
	 * @param id the ID of the user
	 * @return the user entity
	 * @throws EntityNotFoundException if the user is not found
	 */
	public User getUserById(long id) {
		return userRepo.getById(id);
	}

	/**
	 * Validates that both the user and the role exist.
	 *
	 * @param user the user entity
	 * @param role the role entity
	 * @throws EntityNotFoundException if either the user or the role does not exist
	 */
	private static void validateUserAndRoleReferences(User user, Role role) {
		if (user == null || role == null) {
			throw new EntityNotFoundException("User or role non existent");
		}
	}
}
