package com.adoptpet.server.community.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateNoteRequest {
    @JsonProperty("roomId")
    private final Integer noteNo;
}
