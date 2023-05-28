package com.adoptpet.server.adopt.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyAdoptResponseDto {

    private Integer id;
    private String title;
    private String context;
    private String author;
    private Integer view;
    private long like;
    private long publishedAt;
    private String thumb;

    public MyAdoptResponseDto(Integer id, String title, String context, String author, Integer view, long like, LocalDateTime publishedAt, String thumb) {
        this.id = id;
        this.title = title;
        this.context = context;
        this.author = author;
        this.view = view;
        this.like = like;
        this.publishedAt = publishedAt.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.thumb = thumb;
    }
}
