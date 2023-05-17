package com.adoptpet.server.adopt.dto.response;

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
    private LocalDateTime regDate;
    private String thumbnail;

}
