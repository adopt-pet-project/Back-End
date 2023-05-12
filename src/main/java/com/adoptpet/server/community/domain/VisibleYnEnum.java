package com.adoptpet.server.community.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum VisibleYnEnum {
    VISIBLE("Y"),
    UNVISIBLE("N");

    @Getter
    @JsonValue// JSON으로 serialize 할 때 enum 값을 해당 값으로 변환
    private final String value;

    VisibleYnEnum(String value){
        this.value = value;
    }


    //"Y","N"을 키로, VisibleYn을 값으로 가지는 Map 생성
    private static final Map<String, VisibleYnEnum> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(VisibleYnEnum::getValue, Function.identity()));

    @JsonCreator// JSON을 deserialize 할 때 해당 메서드를 이용해 값을 변환
    public static VisibleYnEnum from(String value){
        return Optional.ofNullable(CODE_MAP.get(value))
                .orElseThrow(()-> new IllegalArgumentException("Invalid value"));
    }

    @Converter// JPA에서 DB와 연동할 때 해당 Converter를 이용해 값을 변환
    public static final class VisibleConverter implements AttributeConverter<VisibleYnEnum,String>{

        @Override //enum을 DB에 저장할 값으로 변환
        public String convertToDatabaseColumn(VisibleYnEnum attribute) {
            return attribute.getValue();
        }

        @Override // DB에 있는 값을 다시 enum으로 변환
        public VisibleYnEnum convertToEntityAttribute(String dbData) {
            return VisibleYnEnum.from(dbData);
        }
    }
}
