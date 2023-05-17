package com.adoptpet.server.commons.security.config;

import com.adoptpet.server.commons.security.config.handler.MyAuthenticationFailureHandler;
import com.adoptpet.server.commons.security.config.handler.MyAuthenticationSuccessHandler;
import com.adoptpet.server.commons.security.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile("local") // Profile이 local인 경우에만 설정이 동작한다.
public class SecurityConfig {
    private final MyAuthenticationSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthFilter jwtAuthFilter;
    private final MyAuthenticationFailureHandler oAuth2LoginFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // HTTP 기본 인증을 비활성화
                .csrf().disable() // CSRF 보호 기능 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션관리 정책을 STATELESS(세션이 있으면 쓰지도 않고, 없으면 만들지도 않는다)
                .and()
                .authorizeRequests() // 요청에 대한 인증 설정
                .antMatchers("/token/**").permitAll() // 토큰 발급을 위한 경로는 모두 허용
                .antMatchers("/", "/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**").permitAll()
                .antMatchers("/user/**").hasAnyRole("MANAGER", "USER") // 회원 페이지는 회원(USER) 또는 관리자(MANAGER) 권한이 있어야 접근 가능
                .antMatchers("/admin/**").hasRole("MANAGER") // 관리자 페이지는 관리자(MANAGER) 권한이 있어야 접근 가능
                .anyRequest().authenticated() // 그 외의 모든 요청은 인증이 필요하다.
                .and()
                .oauth2Login() // OAuth2 로그인 설정시작
                .userInfoEndpoint().userService(customOAuth2UserService) // OAuth2 로그인시 사용자 정보를 가져오는 엔드포인트와 사용자 서비스를 설정
                .and()
                .failureHandler(oAuth2LoginFailureHandler) // OAuth2 로그인 실패시 처리할 핸들러를 지정해준다.
                .successHandler(oAuth2LoginSuccessHandler); // OAuth2 로그인 성공시 처리할 핸들러를 지정해준다.



        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가한다.
        return http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    // CORS 설정
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://15.164.216.101/"); // CORS 허용할 오리진 추가
        configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
        configuration.addAllowedMethod("*"); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 자격 증명 허용 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 구성 적용
        return source;
    }




}
