package com.adoptpet.server.commons.security;

import com.adoptpet.server.adopt.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return
                http
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                        .csrf().disable()
                        .headers().frameOptions().disable()
                        .and()
                        .authorizeRequests()
                        .antMatchers("/", "/css/**", "images/**", "/js/**", "/h2-console/**").permitAll()
                        .anyRequest()
                        .authenticated()
                        .and()
                        .logout().logoutSuccessUrl("/")
                        .and()
                        .oauth2Login()
                        .userInfoEndpoint()
                        .userService(customOAuth2UserService)
                        .and()
                        .and()
                        .build();

    }
}
