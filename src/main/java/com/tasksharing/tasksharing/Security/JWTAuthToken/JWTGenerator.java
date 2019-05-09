package com.tasksharing.tasksharing.Security.JWTAuthToken;

import com.tasksharing.tasksharing.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTGenerator {
    private final Logger logger = LoggerFactory.getLogger(JWTGenerator.class);
    public String generate(User jwtUser) {

        Claims claims = Jwts.claims()
                .setSubject(jwtUser.getUserName());
        claims.put("privileges", jwtUser.getPrivileges());
        claims.put("userId", jwtUser.getId());

        String j = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, "cokkgizli")
                .setExpiration(new Date(System.currentTimeMillis() + 86_400_000))
                .compact();
        logger.info(j);
        return j;
    }
}