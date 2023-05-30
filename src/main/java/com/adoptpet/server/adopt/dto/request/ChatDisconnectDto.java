package com.adoptpet.server.adopt.dto.request;


import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ChatDisconnectDto {

    @NotNull
    private Integer chatRoomNo;

    @NotNull
    private String email;
}
