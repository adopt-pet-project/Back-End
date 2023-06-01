package com.adoptpet.server.community.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class SendNoteRequest {

    @Min(value = 0)
    @JsonProperty("targetId")
    private final Integer receiverNo;
    @NotBlank
    @JsonProperty("contents")
    private final String content;
}
