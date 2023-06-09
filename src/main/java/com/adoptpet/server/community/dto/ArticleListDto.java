package com.adoptpet.server.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListDto {

    @JsonProperty("id")
    private Integer articleNo; // community
    @JsonProperty("title")
    private String title; // community
    @JsonProperty("context")
    private String content; // community
    @JsonProperty("author")
    private String nickname;// member
    @JsonProperty("view")
    private Integer viewCount; // community
    @JsonProperty("comment")
    private Integer commentCnt; // Comment
    @JsonProperty("like")
    private Integer likeCnt; // articleHeart
    @JsonProperty("publishedAt")
    private LocalDateTime regDate; //community
    @JsonProperty("thumbnail")
    private String thumbnail; // community
}
