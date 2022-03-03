package com.mile.pc.mile.restoraunt.app.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleToUser {
	
	@NotNull(message = "please specify a role.")
	private String type;
	private String username;
}
