package com.adoptpet.server.adopt.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class ChatDisconnectDto {

    @NotNull
    private Integer chatRoomNo;

    @NotNull
    private String email;
}
