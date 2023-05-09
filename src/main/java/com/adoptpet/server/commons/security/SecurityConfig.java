package com.adoptpet.server.commons.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/board").hasRole("USER") // hasRole을 사용하면 ROLE_ prefix가 붙는다
                .antMatchers("/admin/**").hasRole("MANAGER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .build();

    }

    /*
     *  In-Memory User 등록 방법 -> WebSecurityConfigurerAdapter는 Deprecated 되었으므로 수정
     * */
    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .username("user")
                .password("{noop}1111")
                .roles("USER")
                .build();

        UserDetails manager = User.builder()
                .username("manager")
                .password("{noop}1111")
                .roles("MANAGER", "USER")
                .build();

        return new InMemoryUserDetailsManager(user, manager);
    }


}
