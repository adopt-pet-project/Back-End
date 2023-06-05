package com.adoptpet.server.adopt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class AdoptImageResponseDto {

    private Integer imgNo;
    private String imgUrl;
}
