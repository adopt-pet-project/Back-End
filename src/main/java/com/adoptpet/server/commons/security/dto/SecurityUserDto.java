package com.adoptpet.server.commons.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class SecurityUserDto {
    private String email;
    private String name;
    private String picture;
    private String role;

    @Builder
    public SecurityUserDto(String email, String name, String picture, String role) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.role = role;
    }
}
