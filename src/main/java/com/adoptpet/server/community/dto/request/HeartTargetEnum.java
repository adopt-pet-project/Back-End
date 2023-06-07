package com.adoptpet.server.community.dto.request;

import com.adoptpet.server.commons.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum HeartTargetEnum {
    ARTICLE("article"), COMMENT("comment");

    private final String value;

    HeartTargetEnum(String value) {
        this.value = value;
    }

    private static final Map<String, HeartTargetEnum> CODE_MAP
            = Stream.of(values())
            .collect(Collectors.toMap(HeartTargetEnum::getValue, Function.identity()));

    // JSON 역직렬화시 해당 메서드를 이용해 값을 변환
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static HeartTargetEnum from(String value){
        return Optional.ofNullable(CODE_MAP.get(value)).orElseThrow(ErrorCode::throwTypeNotFound);
    }
}
