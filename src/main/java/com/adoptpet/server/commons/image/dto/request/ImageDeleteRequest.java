package com.adoptpet.server.commons.image.dto.request;

import com.adoptpet.server.commons.image.ImageTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true) //final 필드는 0/false/null 로 초기화
public class ImageDeleteRequest {
    @JsonProperty("type")
    private final ImageTypeEnum type;

    @JsonProperty("fileName")
    private String fileName;
}
