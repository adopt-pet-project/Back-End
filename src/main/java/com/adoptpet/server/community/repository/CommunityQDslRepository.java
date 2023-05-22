package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.dto.ArticleDetailInfo;
import com.adoptpet.server.community.dto.QArticleDetailInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.adoptpet.server.community.domain.QArticleBookmark.articleBookmark;
import static com.adoptpet.server.community.domain.QArticleHeart.articleHeart;
import static com.adoptpet.server.community.domain.QCommunity.community;
import static com.adoptpet.server.user.domain.QMember.member;
import static com.adoptpet.server.user.domain.QProfileImage.profileImage;
import static com.adoptpet.server.community.domain.QComment.comment;

@Repository
public class CommunityQDslRepository {

    private final JPAQueryFactory query;

    public CommunityQDslRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    public void deleteBookmark(Community community){
        query.delete(articleBookmark)
                .where(articleBookmark.community.eq(community))
                .execute();
    }

    public void deleteArticleLike(Community community){
        query.delete(articleHeart)
                .where(articleHeart.community.eq(community))
                .execute();
    }


    //== 게시글 상세내용 Join ==//
    public ArticleDetailInfo findArticleDetail(Integer articleNo){
        return query
                .select(new QArticleDetailInfo(
                        community.articleNo,
                        community.title,
                        member.nickname,
                        member.memberNo,
                        profileImage.imageUrl.as("profile"),
                        community.viewCount.as("view"),
                        articleHeart.heartNo.countDistinct().intValue().as("like"),
                        comment.commentNo.countDistinct().intValue().as("comment"),
                        community.regDate,
                        community.content
                ))
                .from(community)
                .join(profileImage).on(community.regId.eq(profileImage.regId))
                .join(member).on(community.regId.eq(member.email))
                .leftJoin(articleHeart).on(community.articleNo.eq(articleHeart.community.articleNo))
                .leftJoin(comment).on(community.articleNo.eq(comment.community.articleNo))
                .where(community.articleNo.eq(articleNo))
                .groupBy(community.articleNo,community.regId,community.title,member.nickname,
                        member.memberNo,community.regDate,profileImage.imageUrl,community.content)
                .fetchFirst();
    }

    //게시글의 소유자인지 이메일로 검증
    public boolean isMine(String email, Integer articleNo) {
            return query.select(community.articleNo)
                    .from(community)
                    .where(community.regId.eq(email), community.articleNo.eq(articleNo))
                    .fetchFirst() != null;
    }
}
