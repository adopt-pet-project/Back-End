package com.adoptpet.server.community.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum CommentTypeEnum {
    COMMENT(0), REPLY(1);

    @JsonValue
    @Getter
    private final Integer value;

    CommentTypeEnum(Integer value) {
        this.value = value;
    }

}
