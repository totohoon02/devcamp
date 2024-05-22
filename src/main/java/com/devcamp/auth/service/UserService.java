package com.devcamp.auth.service;

import com.devcamp.auth.dto.SignupRequestDto;
import com.devcamp.auth.dto.SignupResponseDto;

public interface UserService {
    SignupResponseDto signup(SignupRequestDto requestDto);
}
