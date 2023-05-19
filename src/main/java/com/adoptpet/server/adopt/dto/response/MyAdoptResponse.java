package com.adoptpet.server.adopt.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyAdoptResponse {

    private Integer id;
    private String title;
    private String context;
    private String author;
    private Integer view;
    private long like;
    private LocalDateTime publishedAt;
    private String thumb;
}
