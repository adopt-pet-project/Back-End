//package com.adoptpet.server.commons.security.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig2 {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return
//                http.authorizeRequests()
//                        .antMatchers("/login").permitAll()
//                        .antMatchers("/admin/**").hasRole("MANAGER")
//                        .antMatchers("user/**").hasAnyRole("MANAGER", "USER")
//                        .and()
//                        .formLogin()
//                        .and()
//                        .build();
//    }
//
//
//     /*
//     *  In-Memory User 등록 방법 -> WebSecurityConfigurerAdapter는 Deprecated 되었으므로 수정
//     * */
//    @Bean
//    public UserDetailsService users() {
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{noop}1111")
//                .roles("USER")
//                .build();
//
//        UserDetails manager = User.builder()
//                .username("manager")
//                .password("{noop}1111")
//                .roles("MANAGER", "USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, manager);
//    }
//}
