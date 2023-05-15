package com.adoptpet.server.commons.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageDto {

    private String path;
    private String imageKey;
    private String imageName;
    private String imageType;
}
