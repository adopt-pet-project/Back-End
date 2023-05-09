package com.adoptpet.server.community.domain;


import com.adoptpet.server.community.dto.request.RegisterArticleRequest;
import com.adoptpet.server.user.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMMUNITY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_no")
    private Integer articleNo;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "view_cnt")
    private Integer viewCount;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "mod_date")
    private LocalDateTime modDate;

    @Column(name = "reg_id")
    private String regId;

    @Column(name = "mod_id")
    private String modId;

    @Enumerated(EnumType.STRING)
    @Column(name = "visible_yn")
    private VisibleYnEnum visibleYn;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "logical_del")
    private LogicalDelEnum logicalDel;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "blind_yn")
    private BlindYnEnum blindYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_no")
    private Category category;

    @OneToMany(mappedBy = "community")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "community")
    private List<ArticleHeart> articleHearts = new ArrayList<>();

    @OneToMany(mappedBy = "community")
    private List<CommunityImage> communityImages = new ArrayList<>();

    @OneToMany(mappedBy = "community")
    private List<ArticleBookmark> articleBookmarks = new ArrayList<>();

    @Builder
    public Community(String title, String content, Integer viewCount,
                     LocalDateTime regDate, LocalDateTime modDate, String regId,
                     String modId, VisibleYnEnum visibleYn, LogicalDelEnum logicalDel,
                     BlindYnEnum blindYn) {
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.regDate = regDate;
        this.modDate = modDate;
        this.regId = regId;
        this.modId = modId;
        this.visibleYn = visibleYn;
        this.logicalDel = logicalDel;
        this.blindYn = blindYn;
    }

    //== 연관관계 메서드 ==//
    public void addMember(Member member){
        this.member = member;
    }

    public void addCategory(Category category){
        this.category = category;
    }

    public void getComment(Comment comment){
        this.comments.add(comment);
    }

    public void getArticleHeart(ArticleHeart articleHeart) {
        this.articleHearts.add(articleHeart);
    }

    public void getArticleBookmark(ArticleBookmark articleBookMark) {
        this.articleBookmarks.add(articleBookMark);
    }

    public void getArticleImage(CommunityImage communityImage) {
        this.communityImages.add(communityImage);
    }

    public void addModeDate(LocalDateTime dateTime){
        this.modDate = dateTime;
    }

    //== 생성 메서드 ==//

    //== 조회 메서드 ==//

    //== 수정 메서드 ==//

    //== 비즈니스 로직 ==//

//    public Community createArticle(RegisterArticleRequest request){
//        Community community = request.toEntity();
//
//        LocalDateTime regDate = community.getRegDate();
//
//        community.addModeDate(regDate);
//
//        return community;
//    }

}
