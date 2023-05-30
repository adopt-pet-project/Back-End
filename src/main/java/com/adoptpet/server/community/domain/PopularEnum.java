package com.adoptpet.server.community.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum PopularEnum {
    HOT(0), WEEKLY(1);

    @JsonValue// JSON으로 serialize 할 때 enum 값을 해당 값으로 변환
    private final Integer value;

    PopularEnum(Integer value) {
        this.value = value;
    }
}
