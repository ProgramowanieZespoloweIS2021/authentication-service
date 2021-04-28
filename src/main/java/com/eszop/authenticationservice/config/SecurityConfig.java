package com.eszop.authenticationservice.config;

import com.eszop.authenticationservice.security.CustomLogoutHandler;
import com.eszop.authenticationservice.security.JwtTokenAuthenticationFilter;
import com.eszop.authenticationservice.security.JwtTokenProvider;
import com.eszop.authenticationservice.security.JwtUsernameAndPasswordAuthenticationFilter;
import com.eszop.authenticationservice.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;

    private final CustomLogoutHandler logoutHandler;

    private final JwtTokenProvider provider;

    @Value("${login.path}")
    private String loginPath;

    public SecurityConfig(CustomUserDetailsService userDetailsService, CustomLogoutHandler logoutHandler, JwtTokenProvider provider) {
        this.userDetailsService = userDetailsService;
        this.logoutHandler = logoutHandler;
        this.provider = provider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable();
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .addFilterAfter(new JwtTokenAuthenticationFilter(provider), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), provider, loginPath))
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/auth/user").permitAll()
                .anyRequest().authenticated()
        .and()
            .logout()
            .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .permitAll();
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
