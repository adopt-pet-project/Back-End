package com.adoptpet.server.adopt.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdoptStatusRequestDto {

    private Integer id;
    private String status;
}
