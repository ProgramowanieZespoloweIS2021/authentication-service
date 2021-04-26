package com.eszop.authenticationservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtTokenProvider provider;

    public CustomLogoutHandler(JwtTokenProvider provider) {
        this.provider = provider;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = provider.resolveToken(request);
        if (token != null) {
            provider.invalidateToken(token);
        }
    }
}
