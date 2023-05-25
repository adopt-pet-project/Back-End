package com.adoptpet.server.community.domain;

import com.adoptpet.server.commons.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum LogicalDelEnum {
    NORMAL(0), DELETE(1), OUT_MEMBER(2);

    @JsonValue// JSON으로 serialize 할 때 enum 값을 해당 값으로 변환
    private final Integer value;

    LogicalDelEnum(Integer value){
        this.value = value;
    }

    // value를 키로, BlindYnEnum을 값으로 가지는 Map 생성
    private static final Map<Integer, LogicalDelEnum> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(LogicalDelEnum::getValue, Function.identity()));

    // JSON을 deserialize 할 때 해당 메서드를 이용해 값을 변환
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static LogicalDelEnum from(Integer value){
        return Optional.ofNullable(CODE_MAP.get(value))
                .orElseThrow(ErrorCode::throwTypeNotFound);
    }
}
