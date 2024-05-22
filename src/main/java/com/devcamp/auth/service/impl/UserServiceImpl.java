package com.devcamp.auth.service.impl;

import com.devcamp.auth.dto.SignupRequestDto;
import com.devcamp.auth.dto.SignupResponseDto;
import com.devcamp.auth.entity.User;
import com.devcamp.auth.repository.UserRepository;
import com.devcamp.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        Optional<User> optionalUser = userRepository.findByEmail(requestDto.getEmail());
        if (optionalUser.isPresent()) {
            throw new RuntimeException(requestDto.getEmail() + "is Already Exist..!");
        }

        // User 저장
        User user = new User(
                requestDto.getEmail(),
                passwordEncoder.encode(requestDto.getPassword()),
                requestDto.getUserRole()
        );
        userRepository.save(user);

        return SignupResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userRole(user.getUserRole())
                .build();
    }
}
