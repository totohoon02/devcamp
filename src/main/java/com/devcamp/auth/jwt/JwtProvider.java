package com.devcamp.auth.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtProvider {
    // Header KEY 값
    public static final String ACCESS_TOKEN_HEADER = "AccessToken";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // Access 토큰 만료시간
    private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분
    // Refresh 토큰 만료시간
    private final long REFRESH_TOKEN_TIME = 60 * 60 * 24 * 7 * 1000L; // 7일

    @Value("${jwt.secret.key}") // lombok이 import 되지 않게 잘 확인해보자
    private String secretKey;
    private SecretKey key;
    private MacAlgorithm algorithm;


    @PostConstruct
    public void init(){
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        algorithm = Jwts.SIG.HS256;
    }

    // 토큰 생성을 위한 정보
    public TokenPayload createTokenPayLoad(String email, TokenType tokenType){
        Date date = new Date();
        long tokenTime = TokenType.ACCESS.equals(tokenType) ? ACCESS_TOKEN_TIME : REFRESH_TOKEN_TIME;
        return new TokenPayload(
                email,
                UUID.randomUUID().toString(),
                date,
                new Date(date.getTime() + tokenTime)
        );
    }

    // 토큰 생성
    public String createToken(TokenPayload payload){
        return BEARER_PREFIX +
                Jwts.builder()
                        .subject(payload.getSub()) // 식별자
                        .issuedAt(payload.getIat()) // 발급일
                        .expiration(payload.getExpiresAt())
                        .id(payload.getJti()) // Jwt ID - uuid
                        .signWith(key, algorithm)
                        .compact();
    }

    // 헤더에서 토큰 가져오기
    public String getJwtFromHeader(HttpServletRequest request, TokenType tokenType){
        String bearerToken = request.getHeader(TokenType.ACCESS.equals(tokenType) ? ACCESS_TOKEN_HEADER : REFRESH_TOKEN_HEADER);

        // 토큰이 있는지 체크
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return false;
    }

    // Claims - 토큰 페이로드의 정보 객체
    public Claims getUserInfoFromToken(String token){
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
