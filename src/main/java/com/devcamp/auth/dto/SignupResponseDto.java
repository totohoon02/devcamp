package com.devcamp.auth.dto;

import com.devcamp.auth.entity.UserRole;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class SignupResponseDto {
    private Long id;
    private String email;
    private UserRole userRole;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
