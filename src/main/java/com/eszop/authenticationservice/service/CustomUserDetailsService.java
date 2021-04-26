package com.eszop.authenticationservice.service;

import com.eszop.authenticationservice.domain.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Value("${userServiceUrl}")
    private String userServiceUrl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = loadAppUserByUsername(username);
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        return new User(appUser.getEmail(), appUser.getPassword(), grantedAuthorities);
    }

    public AppUser loadAppUserByUsername(String username) throws UsernameNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        AppUser[] response = restTemplate.getForObject(userServiceUrl + "/users?email=" + username, AppUser[].class);
        if (response == null || response[0] == null) {
            throw new UsernameNotFoundException("Username: " + username + " not found");
        }
        return response[0];
    }
}
