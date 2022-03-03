package com.mile.pc.mile.restoraunt.app.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {
	
	@NotNull
    @NotEmpty
	private String firstName;
	@NotNull
    @NotEmpty
	private String username;
	@NotNull
    @NotEmpty		
	private String password;
}
