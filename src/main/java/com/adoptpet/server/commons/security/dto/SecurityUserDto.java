package com.adoptpet.server.commons.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SecurityUserDto {
    private String email;
    private String name;
    private String picture;

    @Builder
    public SecurityUserDto(String email, String name, String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
    }
}
