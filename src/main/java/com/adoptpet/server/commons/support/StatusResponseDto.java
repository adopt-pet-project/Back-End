package com.adoptpet.server.commons.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StatusResponseDto {
    private Integer status;



    public static StatusResponseDto addStatus(Integer status) {
        return new StatusResponseDto(status);
    }
}
