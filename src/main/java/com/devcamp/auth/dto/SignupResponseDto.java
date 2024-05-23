package com.devcamp.auth.dto;

import com.devcamp.auth.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SignupResponseDto {
    private Long id;
    private String email;
    private UserRole userRole;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
