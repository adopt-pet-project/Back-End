package com.adoptpet.server.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailInfo {

    private Integer articleNo; //  community
    private String regId; // community
    private String title; //  community
    private String nickname; //  member
    private Integer view; // community
    private Integer like; // articleHeart
    private Integer comment; // comment
    private LocalDateTime regDate; // community
    private String profile; // profileImage
    private String content; // community
    private List<String> image; // articleImage

}
