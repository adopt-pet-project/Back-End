package com.adoptpet.server.community.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RegisterCommentRequest {

    @Min(value = 0)
    @JsonAlias("boardId")
    private Integer articleNo;

    @Min(value = 0)
    @JsonAlias("parentId")
    private Integer parentNo;

    @NotNull
    @JsonAlias("context")
    private String content;
}
