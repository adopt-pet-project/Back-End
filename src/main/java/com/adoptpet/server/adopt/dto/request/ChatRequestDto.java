package com.adoptpet.server.adopt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@AllArgsConstructor
public class ChatRequestDto {

    @NotNull
    private Integer saleNo;
    @NotNull
    private Integer createMember;

}
