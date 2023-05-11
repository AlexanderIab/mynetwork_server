package com.iablonski.mynetwork.security;

// this class creates token

import com.iablonski.mynetwork.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTokenProvider {
    public static final Logger LOG = LoggerFactory.getLogger(JWTokenProvider.class);

    private Key getKey() {
        byte[] keyByte = Decoders.BASE64.decode(SecurityConstants.SECRET_JWT);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        // Объект, который мы передаем в JWT
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("username", user.getUsername());
        claimsMap.put("fullName", user.getFullName());

        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getKey())
                .compact();
    }

    public boolean validateToken(String token) {
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

    public Long getUserIdFromToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey()).build()
                .parseClaimsJws(token)
                .getBody();
        String id = (String) claims.get("id");
        return Long.parseLong(id);
    }
}
