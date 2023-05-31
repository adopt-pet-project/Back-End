package com.adoptpet.server.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class MyCommentResponseDto {
    @JsonProperty("id")
    private Integer commentNo;
    @JsonProperty("refId")
    private Integer articleNo;
    @JsonProperty("title")
    private String articleContent;
    @JsonProperty("contents")
    private String commentContent;
    @JsonProperty("publishedAt")
    private String publishedAt;
}
