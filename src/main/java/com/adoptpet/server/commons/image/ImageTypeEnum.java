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

    private static final Map<String, ImageTypeEnum> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(ImageTypeEnum::getType, Function.identity()));

    @JsonCreator// JSON을 deserialize 할 때 해당 메서드를 이용해 값을 변환
    public static ImageTypeEnum from(String type){
        return Optional.ofNullable(CODE_MAP.get(type))
                .orElseThrow(()-> new IllegalArgumentException("요청한 type의 값이 유효하지 않습니다."));
    }
}
