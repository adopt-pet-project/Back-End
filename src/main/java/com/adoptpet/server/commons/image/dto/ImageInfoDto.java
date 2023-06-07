package com.adoptpet.server.commons.image.dto;

import com.adoptpet.server.commons.image.dto.response.ImageUploadResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageInfoDto {

    private Integer imageNo;
    private String imageUrl;

    public ImageUploadResponse toResponse(){
        return new ImageUploadResponse(this.imageNo,this.imageUrl);
    }
}
