package com.adoptpet.server.adopt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdoptRenderResponseDto {

    private Integer id;
    private String name;
    private String age;
    private String kind;
    private Float latitude;
    private Float longitude;
    private String thumbnail;
}
