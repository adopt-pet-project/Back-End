package com.adoptpet.server.adopt.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdoptImageRequestDto {

    private Integer imgNo;
    private String imgUrl;
}
