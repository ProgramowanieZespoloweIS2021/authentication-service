package com.eszop.authenticationservice.service;

import com.eszop.authenticationservice.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Value("${userServiceUrl}")
    private String userServiceUrl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        RestTemplate restTemplate = new RestTemplate();
        AppUser[] response = restTemplate.getForObject(userServiceUrl + "/users?email=" + username, AppUser[].class);
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");
        if (response == null || response[0] == null) {
            throw new UsernameNotFoundException("Username: " + username + " not found");
        }
        AppUser appUser = response[0];

        return new User(appUser.getEmail(), appUser.getPassword(), grantedAuthorities);
    }

    public void registerUser(AppUser user) {
        AppUser userToRegister = encryptUserPassword(user);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<AppUser> request = new HttpEntity<>(userToRegister);
        restTemplate.postForObject(userServiceUrl + "/users", request, AppUser.class);
    }

    private AppUser encryptUserPassword(AppUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return user;
    }
}
