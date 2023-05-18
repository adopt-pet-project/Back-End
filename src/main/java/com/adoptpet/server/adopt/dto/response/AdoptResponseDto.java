package com.adoptpet.server.adopt.dto.response;

import com.adoptpet.server.adopt.domain.AdoptStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdoptResponseDto {

    private Integer id;
    private String title;
    private String kind;
    private String location;
    private long bookmark;
    private long chat;
    private LocalDateTime publishedAt;
    private String thumbnail;
    private String species;
    private Integer status;

}
