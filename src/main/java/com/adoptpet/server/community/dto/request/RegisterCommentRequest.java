package com.adoptpet.server.community.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RegisterCommentRequest {

    @Min(value = 0)
    @JsonProperty("boardId")
    private Integer articleNo;

    @Min(value = 0)
    @JsonProperty("parentId")
    private Integer parentNo;

    @NotNull
    @JsonProperty("context")
    private String content;
}
