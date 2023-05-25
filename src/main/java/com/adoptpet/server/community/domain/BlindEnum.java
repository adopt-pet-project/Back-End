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
public enum BlindEnum {
    NORMAL(0), BLIND(1), TEMP_BLIND(2);

    @JsonValue// JSON으로 serialize 할 때 enum 값을 해당 값으로 변환
    private final Integer value;

    BlindEnum(Integer value){
        this.value = value;
    }

    // value를 키로, BlindYnEnum을 값으로 가지는 Map 생성
    private static final Map<Integer, BlindEnum> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(BlindEnum::getValue, Function.identity()));

    // JSON을 deserialize 할 때 해당 메서드를 이용해 값을 변환
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static BlindEnum from(Integer value){
        return Optional.ofNullable(CODE_MAP.get(value))
                .orElseThrow(ErrorCode::throwTypeNotFound);
    }
}
