package com.adoptpet.server.community.dto;

import com.adoptpet.server.commons.image.dto.ImageInfoDto;
import com.adoptpet.server.community.dto.response.ArticleInfoResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDetailInfoDto {

    private Integer articleNo; //  community
    private boolean mine;
    private String title; //  community
    private String nickname; //  member
    private Integer memberNo; // member
    private String profile; // profileImage
    private Integer view; // community
    private Integer like; // articleHeart
    private Integer comment; // comment
    private LocalDateTime regDate; // community
    private String content; // community
    private List<ImageInfoDto> images;// communityImage

    public void addImages(List<ImageInfoDto> images){
        this.images = images;
    }

    @QueryProjection
    public ArticleDetailInfoDto(Integer articleNo, String title, String nickname, Integer memberNo, String profile, Integer view, Integer like, Integer comment, LocalDateTime regDate, String content) {
        this.articleNo = articleNo;
        this.title = title;
        this.nickname = nickname;
        this.memberNo = memberNo;
        this.profile = profile;
        this.view = view;
        this.like = like;
        this.comment = comment;
        this.regDate = regDate;
        this.content = content;
    }



    public ArticleInfoResponse toResponse(){

        ArticleInfoResponse.Context context = ArticleInfoResponse.Context.builder()
                .context(this.content)
                .imageList(this.images)
                .build();

        ArticleInfoResponse.Header header = ArticleInfoResponse.Header.builder()
                .title(this.title)
                .authorId(this.memberNo)
                .username(this.nickname)
                .profile(this.profile)
                .view(this.view)
                .like(this.like)
                .comment(this.comment)
                .publishedAt(this.regDate)
                .build();

        return ArticleInfoResponse.builder()
                .articleNo(this.articleNo)
                .mine(this.mine)
                .header(header)
                .context(context)
                .build();
    }

    public void addIsMine(boolean mine){
        this.mine = mine;
    }
}
