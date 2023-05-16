package com.adoptpet.server.commons.image;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public enum ImageTypeEnum {
    COMMUNITY("community", "community"),
    ADOPT("adopt", "adopt"),
    PROFILE("user/profile", "profile");

    private final String path;
    private final String type;

    // 타입(type)을 기반으로 Enum 값을 찾기 위한 맵
    private static final Map<String, ImageTypeEnum> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(ImageTypeEnum::getType, Function.identity()));

    @JsonCreator // JSON을 deserialize 할 때, 해당 메서드를 이용하여 값을 변환하는데 사용되는 애노테이션(JsonCreator)
    public static ImageTypeEnum from(String type){
        // 주어진 타입(type)에 해당하는 ImageTypeEnum 값을 반환
        return Optional.ofNullable(CODE_MAP.get(type))
                .orElseThrow(()-> new IllegalArgumentException("요청한 type의 값이 유효하지 않습니다."));
    }
}
