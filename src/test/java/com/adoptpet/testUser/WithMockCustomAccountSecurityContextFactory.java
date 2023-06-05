package com.adoptpet.testUser;

import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithMockCustomAccountSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomAccount> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomAccount customOAuth2Account) {
        // 1
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // 2
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("username", customOAuth2Account.username());
        attributes.put("name", customOAuth2Account.name());
        attributes.put("email", customOAuth2Account.email());

        // 3
        SecurityUserDto principal = SecurityUserDto.builder()
                .memberNo(1)
                .nickname(customOAuth2Account.name())
                .email(customOAuth2Account.email())
                .role("ROLE_USER")
                .build();

        // 4
        Authentication token = new UsernamePasswordAuthenticationToken(principal, "",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        // 5
        context.setAuthentication(token);
        return context;
    }
}
