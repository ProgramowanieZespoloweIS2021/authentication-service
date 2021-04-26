package com.eszop.authenticationservice.security;

import com.eszop.authenticationservice.domain.BlacklistedJwt;
import com.eszop.authenticationservice.repository.BlacklistedJwtRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final BlacklistedJwtRepository blacklistedJwtRepository;

    @Value("${jwt.token.expiration}")
    private long expirationTime;

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public JwtTokenProvider(BlacklistedJwtRepository blacklistedJwtRepository) {
        this.blacklistedJwtRepository = blacklistedJwtRepository;
    }

    public String generateToken(Authentication auth) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(auth.getName())
                .claim("authorities", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return token;
        }
        return null;
    }

    private Claims getAllClaimsFromToken(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
        return jwtParser.parseClaimsJws(token).getBody();
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
            if (claims.getExpiration().before(new Date())) {
                return false;
            }
            return blacklistedJwtRepository.findById(token).isEmpty();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Expired or invalid JWT token");
        }
    }

    public void invalidateToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            if (claims.getExpiration().after(new Date())) {
                BlacklistedJwt blacklistedJwt = new BlacklistedJwt(token, claims.getExpiration());
                blacklistedJwtRepository.save(blacklistedJwt);
            }
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Expired or invalid JWT token");
        }
    }
}
