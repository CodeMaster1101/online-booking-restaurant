package com.mile.pc.mile.restoraunt.app.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;

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
	CustomDTOservice customDTOservice;
	@Test
	void testAddRoleToUser() {
		User user = new User(3l, "testUser", "x", 123, null, new HashSet<>(), null);
		Role role =(new Role(2l, "WAITER"));
		when(userRepository.findByUsername("testUser")).thenReturn(user);
		when(roleRepository.findByType("WAITER")).thenReturn(role);
		crudService.AddRoleToUser(user.getUsername(), role.getType());
		verify(userRepository, times(2)).findByUsername("testUser");
		verify(roleRepository, times(2)).findByType("WAITER");
	}

	@Test
	void testRemoveRolefromUser() {
		User user = new User(3l, "testUser", "x", 123, null, new HashSet<>(), null);
		Role role =(new Role(2l, "WAITER"));
		when(userRepository.findByUsername("testUser")).thenReturn(user);
		when(roleRepository.findByType("WAITER")).thenReturn(role);
		crudService.removeRolefromUser(user.getUsername(), role.getType());
		verify(userRepository, times(1)).findByUsername("testUser");
		verify(roleRepository, times(1)).findByType("WAITER");
	}

	@Test
	void testRemoveUser() {
		User user = new User(3l, "testUser", "x", 123, null, new HashSet<>(), null);
		crudService.removeUser(user.getId());
		verify(userRepository, times(1)).deleteById(user.getId());
	}

}
