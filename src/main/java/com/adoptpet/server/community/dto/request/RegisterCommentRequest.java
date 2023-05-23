package com.adoptpet.server.community.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterCommentRequest {

    @JsonProperty("boardId")
    private Integer articleNo;

    @JsonProperty("parentId")
    private Integer parentNo;

    @JsonProperty("context")
    private String content;
}
