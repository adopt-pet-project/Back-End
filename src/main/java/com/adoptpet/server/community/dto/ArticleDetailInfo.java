package com.adoptpet.server.community.dto;

import lombok.*;

import java.time.LocalDateTime;

@ToString
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

}
