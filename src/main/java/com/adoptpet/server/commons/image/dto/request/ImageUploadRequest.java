package com.adoptpet.server.commons.image.dto.request;

import com.adoptpet.server.commons.image.ImageTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ImageUploadRequest {

    @JsonProperty("contentNo")
    private Integer contentNo;

    @JsonProperty("type")
    private final ImageTypeEnum type;
}
