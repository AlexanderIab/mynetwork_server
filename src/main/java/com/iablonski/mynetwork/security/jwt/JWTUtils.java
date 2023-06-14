package com.iablonski.mynetwork.security.jwt;

// this class creates token

import com.iablonski.mynetwork.security.SecurityConstants;
import com.iablonski.mynetwork.security.service.UserDetailsImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtils {
    public static final Logger LOG = LoggerFactory.getLogger(JWTUtils.class);

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

        String userId = Long.toString(userPrincipal.getId());

        // Объект, который мы передаем в JWT
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("username", userPrincipal.getUsername());
        claimsMap.put("authorities", userPrincipal.getAuthorities());

        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getKey())
                .compact();
    }

    public boolean validateJWToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException |
                 MalformedJwtException |
                 ExpiredJwtException |
                 UnsupportedJwtException |
                 IllegalArgumentException exception) {
            LOG.error(exception.getMessage());
            return false;
        }
    }

    public String getUsernameFromJWToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey()).build()
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("username");
    }

    private Key getKey() {
        byte[] keyByte = Decoders.BASE64.decode(SecurityConstants.SECRET_JWT);
        return Keys.hmacShaKeyFor(keyByte);
    }
}