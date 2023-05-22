package com.adoptpet.server.commons.image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadResponse {

    private Integer id;
    private String url;

}
