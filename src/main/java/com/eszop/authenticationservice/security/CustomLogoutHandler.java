package com.eszop.authenticationservice.security;

import com.eszop.authenticationservice.domain.BlacklistedJwt;
import com.eszop.authenticationservice.repository.BlacklistedJwtRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final BlacklistedJwtRepository blacklistedJwtRepository;

    private final JwtTokenProvider provider;

    public CustomLogoutHandler(BlacklistedJwtRepository blacklistedJwtRepository, JwtTokenProvider provider) {
        this.blacklistedJwtRepository = blacklistedJwtRepository;
        this.provider = provider;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Claims claims = provider.getAllClaimsFromToken(token);
            if (claims.getExpiration().after(new Date())) {
                BlacklistedJwt blacklistedJwt = new BlacklistedJwt(token, claims.getExpiration());
                blacklistedJwtRepository.save(blacklistedJwt);
            }
        }
    }
}
