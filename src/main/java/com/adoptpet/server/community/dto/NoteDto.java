package com.adoptpet.server.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class NoteDto {
    @JsonProperty("id")
    private Integer noteNo; // note
    @JsonProperty("name")
    private String nickName; // member
    @JsonProperty("contents")
    private String content; // noteHistory
    @JsonProperty("mine")
    private boolean mine;
    @JsonProperty("publishedAt")
    private LocalDateTime regDate; // noteHistory
    @JsonProperty("checked")
    private boolean readStatus; // note
}
