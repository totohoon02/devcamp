package com.devcamp.auth.service.impl;

import com.devcamp.auth.entity.User;
import com.devcamp.auth.entity.UserRole;
import com.devcamp.auth.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void 회원가입() throws Exception{
        // given
        User user = new User("totohoon01@naver.com", "1234", UserRole.USER);
        userRepository.save(user);

        // when
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        // then
        Assertions.assertThat(optionalUser).isPresent();
        Assertions.assertThat(optionalUser.get().getId()).isEqualTo(user.getId());
    }
}