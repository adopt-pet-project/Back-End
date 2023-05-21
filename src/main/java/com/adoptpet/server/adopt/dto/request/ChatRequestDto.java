package com.adoptpet.server.adopt.dto.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class ChatRequestDto {

    @NotNull
    private Integer saleNo;
    @NotNull
    private Integer createMember;

}
