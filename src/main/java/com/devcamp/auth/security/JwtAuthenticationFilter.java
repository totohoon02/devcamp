package com.devcamp.auth.security;

import com.devcamp.auth.dto.LoginRequestDto;
import com.devcamp.auth.entity.User;
import com.devcamp.auth.jwt.JwtProvider;
import com.devcamp.auth.jwt.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;

        // 로그인 처리 주소
        setFilterProcessesUrl("/auth/login");
    }

    // ctrl + o
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            System.out.println(requestDto.getEmail());
            System.out.println(requestDto.getPassword());

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    requestDto.getEmail(),
                    requestDto.getPassword()
            );

            System.out.println(token);
            AuthenticationManager authenticationManager = getAuthenticationManager();

            System.out.println("1231212" + authenticationManager);

            Authentication authentication = authenticationManager.authenticate(token);

            System.out.println(authentication);
            // name,pw를 이용해 Authentication 반환
            return authentication;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println(1111);
        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();
        String email = user.getEmail();
        String accessToken = jwtProvider.createToken(jwtProvider.createTokenPayLoad(email, TokenType.ACCESS));
        String refreshToken = jwtProvider.createToken(jwtProvider.createTokenPayLoad(email, TokenType.REFRESH));

        // 헤더에 정보 포함
        response.addHeader(JwtProvider.ACCESS_TOKEN_HEADER, accessToken);
        response.addHeader(JwtProvider.REFRESH_TOKEN_HEADER, refreshToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println(2222);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
