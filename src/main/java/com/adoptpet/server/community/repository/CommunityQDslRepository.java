package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.QCommunity;
import com.adoptpet.server.community.dto.ArticleDetailInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.criterion.Projection;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.adoptpet.server.community.domain.QArticleHeart.articleHeart;
import static com.adoptpet.server.community.domain.QCommunity.community;
import static com.adoptpet.server.community.domain.QCommunityImage.communityImage;
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
                        articleHeart.heartNo.count(),
                        comment.commentNo.count(),
                        community.regDate,
                        profileImage.imageUrl,
                        community.content,
                        communityImage.imageUrl
                ))
                .from(community)
                .join(community).on(profileImage.regId.eq(community.regId))
                .join(community).on(member.email.eq(community.regId))
                .join(community,articleHeart.community)
                .join(community,comment.community)
                .join(community).on(communityImage.articleNo.eq(community.articleNo))
                .fetchOne();
    }
}
