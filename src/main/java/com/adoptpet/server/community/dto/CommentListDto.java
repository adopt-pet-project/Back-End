package com.adoptpet.server.community.dto;

import com.adoptpet.server.community.domain.BlindEnum;
import com.adoptpet.server.community.domain.LogicalDelEnum;
import com.adoptpet.server.community.dto.response.CommentListResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class CommentListDto {
    @JsonProperty("type")
    private String type;
    @JsonProperty("id")
    private Integer commentNo;
    @JsonProperty("like")
    private Integer commentHeart;
    @JsonProperty("author")
    private String nickname;
    @JsonProperty("authorId")
    private Integer memberId;
    @JsonProperty("context")
    private String content;
    @JsonProperty("profile")
    private String profile;
    @JsonProperty("publishedAt")
    private LocalDateTime regDate;
    @JsonProperty("deleteStatus")
    private LogicalDelEnum logicalDel;
    @JsonProperty("blindStatus")
    private BlindEnum blindYn;
    @JsonIgnore
    private List<CommentListDto> childComment;

    public void addChildCommentList(List<CommentListDto> childComment) {
        this.childComment = childComment;
    }

    public CommentListResponse toResponse(){

        return CommentListResponse.builder()
                .type(this.type)
                .id(this.commentNo)
                .author(this.nickname)
                .authorId(this.memberId)
                .context(this.content)
                .profile(this.profile)
                .publishedAt(this.regDate)
                .like(this.commentHeart)
                .deleteStatus(this.logicalDel)
                .blindStatus(this.blindYn)
                .comments(this.childComment)
                .build();
    }
}
