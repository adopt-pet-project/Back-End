package com.adoptpet.server.commons.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // DTO 를 JSON으로 변환 시 null값인 field 제외
public class StatusResponseDto {
    private Integer status;
    private String massage;

    public StatusResponseDto(Integer status) {
        this.status = status;
    }

    public static StatusResponseDto addStatus(Integer status) {
        return new StatusResponseDto(status);
    }

    public static StatusResponseDto success(){
        return new StatusResponseDto(200);
    }
    public static StatusResponseDto success(String massage){
        return new StatusResponseDto(200, massage);
    }
}
