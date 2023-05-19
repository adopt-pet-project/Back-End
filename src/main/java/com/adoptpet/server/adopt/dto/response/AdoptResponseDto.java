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

    public AdoptResponseDto(Integer id, String title, String kind, String location, long bookmark, long chat, LocalDateTime regDate) {
        this.id = id;
        this.title = title;
        this.kind = kind;
        this.location = location;
        this.bookmark = bookmark;
        this.chat = chat;
        this.regDate = regDate;
    }

    public void addThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
