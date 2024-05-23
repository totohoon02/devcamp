package com.devcamp.auth.dto;

import com.devcamp.auth.entity.UserRole;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String email;
    private String password;
    private UserRole userRole = UserRole.USER;
}