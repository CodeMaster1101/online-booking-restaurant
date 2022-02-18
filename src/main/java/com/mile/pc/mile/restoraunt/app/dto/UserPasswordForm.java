package com.mile.pc.mile.restoraunt.app.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordForm {
 private String username;
 private String password;
}
