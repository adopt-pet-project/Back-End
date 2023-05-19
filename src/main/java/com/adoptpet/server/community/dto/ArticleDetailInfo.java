package com.adoptpet.server.community.dto;

import com.adoptpet.server.community.dto.response.ArticleInfoResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
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
    private List<String> images;// communityImage

    public void addImages(List<String> images){
        this.images = images;
    }

    @QueryProjection
    public ArticleDetailInfo(Integer articleNo, String regId, String title, String nickname, Integer view, Integer like, Integer comment, LocalDateTime regDate, String profile, String content) {
        this.articleNo = articleNo;
        this.regId = regId;
        this.title = title;
        this.nickname = nickname;
        this.view = view;
        this.like = like;
        this.comment = comment;
        this.regDate = regDate;
        this.profile = profile;
        this.content = content;
    }

    public ArticleInfoResponse toResponse(){

        ArticleInfoResponse.Context context = ArticleInfoResponse.Context.builder()
                .content(this.content)
                .image(this.images)
                .build();

        ArticleInfoResponse.Header header = ArticleInfoResponse.Header.builder()
                .author(this.nickname)
                .profile(this.profile)
                .title(this.title)
                .view(this.view)
                .like(this.like)
                .comment(this.comment)
                .regDate(this.regDate)
                .build();

        return ArticleInfoResponse.builder()
                .articleNo(this.articleNo)
                .authorId(this.regId)
                .header(header)
                .context(context)
                .build();
    }
}
