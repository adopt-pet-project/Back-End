package com.adoptpet.server.community.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
@Getter
public enum CommentTypeEnum {
    COMMENT(0), REPLY(1);

    @JsonValue
    private final Integer value;

    CommentTypeEnum(Integer value) {
        this.value = value;
    }

}
