package com.eszop.authenticationservice.security;

import com.eszop.authenticationservice.domain.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.token.expiration}")
    private long expirationTime;

    @Value("${security.jwt.secret:JwtSecretKey}")
    private String secret;

    private final CustomUserDetailsService userDetailsService;

    public String generateToken(AppUser user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("id", user.getId());
        claims.put("role", user.getRole());

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setExpiration(expirationDate)
                   .signWith(SignatureAlgorithm.HS256, secret)
                   .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getAllClaimsFromToken(token);
        String username = claims.getSubject();
        List<String> authorities = (List<String>) claims.get("authorities");
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Expired or invalid JWT token");
        }
    }
}
