package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.dto.ArticleDetailInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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

    public ArticleDetailInfo findArticleDetail(Integer articleNo){
        return query
                .select(Projections.constructor(ArticleDetailInfo.class,
                        community.articleNo,
                        community.regId,
                        community.title,
                        member.nickname,
                        articleHeart.heartNo.count().intValue().as("view"),
                        articleHeart.heartNo.count().intValue().as("like"),
                        comment.commentNo.count().intValue().as("comment"),
                        community.regDate,
                        profileImage.imageUrl.as("profile"),
                        community.content
                ))
                .from(community)
                .join(profileImage).on(community.regId.eq(profileImage.regId))
                .join(member).on(community.regId.eq(member.email))
                .leftJoin(articleHeart).on(community.articleNo.eq(articleHeart.community.articleNo))
                .leftJoin(comment).on(community.articleNo.eq(comment.community.articleNo))
                .where(community.articleNo.eq(articleNo))
                .groupBy(community.articleNo,community.regId,community.title,member.nickname,
                        community.regDate,profileImage.imageUrl,community.content)
                .fetchOne();
    }
}
