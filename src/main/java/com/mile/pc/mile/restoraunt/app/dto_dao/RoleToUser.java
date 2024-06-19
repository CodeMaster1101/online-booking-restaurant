package com.mile.pc.mile.restoraunt.app.dto_dao;

import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleToUser {
	
	@NotNull(message = "please specify a role.")
	private String type;

	private String username;

}
