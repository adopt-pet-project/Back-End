package com.adoptpet.server.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CommentListResponse {

    private String type; // comment
    private String id; //commentNo
    private String author; // member
    private Integer authorId; // memberNo
    private String context; // comment
    private String profile; // profileImage
    private LocalDateTime publishedAt; // comment
    private Integer like; // commentHeart
    private List<ChildComment> comments;

    @Builder
    public CommentListResponse(String type, String id, String author, Integer authorId, String context, String profile, LocalDateTime publishedAt, Integer like, List<ChildComment> comments) {
        this.type = type;
        this.id = id;
        this.author = author;
        this.authorId = authorId;
        this.context = context;
        this.profile = profile;
        this.publishedAt = publishedAt;
        this.like = like;
        this.comments = comments;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ChildComment{

        private String type; //reply
        private Integer id; //commentNo
        private String author; // member
        private Integer authorId; // memberNo
        private String context; // comment
        private String profile; // profileImage
        private LocalDateTime publishedAt; // comment
        private Integer like; // commentHeart
    }
}
