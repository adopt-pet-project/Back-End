package com.adoptpet.server.adopt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class AdoptRenderResponseDto {

    private Integer id;
    private String name;
    private String age;
    private String kind;
    private Float latitude;
    private Float longitude;
    private String thumbnail;
    private boolean isModal = false;

    public AdoptRenderResponseDto(Integer id, String name, String age, String kind, Float latitude, Float longitude, String thumbnail) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.kind = kind;
        this.latitude = latitude;
        this.longitude = longitude;
        this.thumbnail = thumbnail;
    }
}
