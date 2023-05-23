package com.adoptpet.server.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListResponse {

    private Integer id; // community
    private String title; // community
    private String context; // community
    private String author; // member
    private Integer view; // Article Heart
    private Integer comment; // Comment
    private LocalDateTime publishedAt; //community
    private String thumbnail; // community
}
