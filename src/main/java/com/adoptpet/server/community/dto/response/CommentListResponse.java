package com.adoptpet.server.community.dto.response;

import com.adoptpet.server.community.domain.BlindEnum;
import com.adoptpet.server.community.domain.LogicalDelEnum;
import com.adoptpet.server.community.dto.CommentListDto;
import com.adoptpet.server.community.dto.CommentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentListResponse {

    private CommentTypeEnum type; // comment
    private boolean mine;
    private Integer id; //commentNo
    private String author; // member
    private Integer authorId; // memberNo
    private String context; // comment
    private String profile; // profileImage
    private LocalDateTime publishedAt; // comment
    private Integer like; // commentHeart
    private LogicalDelEnum deleteStatus; // comment
    private BlindEnum blindStatus; // comment
    private List<CommentListDto> comments;
}
