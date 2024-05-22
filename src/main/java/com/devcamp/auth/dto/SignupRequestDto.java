package com.devcamp.auth.dto;

import com.devcamp.auth.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignupRequestDto {
    private String email;
    private String password;
    private UserRole userRole = UserRole.USER;
}