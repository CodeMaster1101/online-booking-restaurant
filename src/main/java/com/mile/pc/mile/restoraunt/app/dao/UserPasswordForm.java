package com.mile.pc.mile.restoraunt.app.dao;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class UserPasswordForm {
 private String username;
 private String password;
}
