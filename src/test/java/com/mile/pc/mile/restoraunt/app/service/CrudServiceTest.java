package com.mile.pc.mile.restoraunt.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.mile.pc.mile.restoraunt.app.model.Role;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.RoleRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

@SpringBootTest

@RunWith(MockitoJUnitRunner.class)
class CrudServiceTest {
	
	@InjectMocks CrudService crudService;
	@Mock UserRepository userRepository;
	@Mock RoleRepository roleRepository;
	
	@Test
	void testAddRoleToUser() {
		User user = new User(3l, "testUser", "x", 123, null, new HashSet<>(), null);
		Role role =(new Role(2l, "WAITER"));
		when(userRepository.findByUsername("testUser")).thenReturn(user);
		when(roleRepository.findByType("WAITER")).thenReturn(role);
		crudService.AddRoleToUser(user.getUsername(), role.getType());
		assertEquals(1, user.getRoles().size());
	}

	@Test
	void testRemoveRolefromUser() {
		Set<Role> set = new HashSet<>();
		Role role =(new Role(2l, "WAITER"));
		set.add(role);
		User user = new User(3l, "testUser", "x", 123, null, set , null);
		when(userRepository.findByUsername("testUser")).thenReturn(user);
		when(roleRepository.findByType("WAITER")).thenReturn(role);
		crudService.removeRolefromUser(user.getUsername(), role.getType());
		verify(roleRepository, times(1)).findByType("WAITER");
		assertEquals(0, user.getRoles().size());

	}

	@Test
	void testRemoveUser() {
		User user = new User(3l, "testUser", "x", 123, null, new HashSet<>(), null);
		crudService.removeUser(user.getId());
		verify(userRepository, times(1)).deleteById(user.getId());
	}

	@Test
	void testGetWaiters() {
		Set<Role> set = new HashSet<>();
		Role role =(new Role(2l, "WAITER"));
		set.add(role);
		List<User> allUsers = new ArrayList<>();
		allUsers.add(new User(3l, "testUser", "x", 123, null, set , null));
		allUsers.add(new User(4l, "testUser2", "y", 123, null, new HashSet<>() , null));
		allUsers.add(new User(5l, "testUser3", "z", 123, null, new HashSet<>() , null));
		when(userRepository.findAll()).thenReturn(allUsers);
		when(roleRepository.findByType(role.getType())).thenReturn(role);
		List<User> waitersOnly = crudService.getWaiters();
		assertEquals(1, waitersOnly.size());
}

}
