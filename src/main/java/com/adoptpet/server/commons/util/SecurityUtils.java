package com.adoptpet.server.commons.util;

import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityUtils {

    public static String getUserId() {
        return ((SecurityUserDto)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getEmail();
    }

    public static SecurityUserDto getUser() {
        return (SecurityUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
