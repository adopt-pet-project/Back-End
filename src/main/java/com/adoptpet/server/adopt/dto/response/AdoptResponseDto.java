package com.adoptpet.server.adopt.dto.response;

import com.adoptpet.server.adopt.domain.AdoptStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdoptResponseDto {

    private Integer id;
    private String title;
    private String address;
    private long bookmark;
    private long chat;
    private long publishedAt;
    private String thumbnail;
    private String species;
    private Integer status;


    public AdoptResponseDto(Integer id, String title, String address, long bookmark, long chat, LocalDateTime publishedAt, String thumbnail, String species, Integer status) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.bookmark = bookmark;
        this.chat = chat;
        this.publishedAt = publishedAt.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.thumbnail = thumbnail;
        this.species = species;
        this.status = status;
    }
}
