package com.adoptpet.server.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class MyArticleResponseDto {
    private Integer id;
    private String title;
    private String contents;
    private String publishedAt;
    private String thumbnail;
    private Integer views;
    private Integer comment;
    private Integer like;
}
