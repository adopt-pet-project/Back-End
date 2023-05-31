package com.adoptpet.server.community.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class SendNoteRequest {

    @Min(value = 0)
    private final Integer receiverNo;
    @NotBlank
    private final String content;
}
