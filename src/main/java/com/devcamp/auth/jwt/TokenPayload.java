package com.devcamp.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class TokenPayload {
    private String sub;
    private String jti;
    private Date iat;
    private Date expiresAt;
}
