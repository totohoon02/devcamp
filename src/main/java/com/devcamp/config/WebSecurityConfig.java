package com.devcamp.config;

import com.devcamp.auth.jwt.JwtProvider;
import com.devcamp.auth.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtProvider jwtProvider;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Security 인증 객체 관리
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        System.out.println(configuration);
        return configuration.getAuthenticationManager();
    }

    // 인증 필터
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        System.out.println(jwtProvider);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtProvider);
        System.out.println(authenticationConfiguration);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    // 인가 필터

    // 필터 체인
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        // CSRF
        security.csrf(AbstractHttpConfigurer::disable);
        security.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        // API 방식으로 인증 / 인가 제어
        security.authorizeHttpRequests((request) -> request
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 리소스 폴더
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
        );

        // JWT 사용
        security.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // FormLogin 비활성화
        security.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        // JWT 필터 등록
        security.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return security.build();
    }


}
