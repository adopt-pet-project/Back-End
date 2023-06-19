package com.adoptpet.server.community.repository;

import com.adoptpet.server.commons.image.dto.ImageInfoDto;
import com.adoptpet.server.community.domain.BlindEnum;
import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.domain.LogicalDelEnum;
import com.adoptpet.server.community.dto.*;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.adoptpet.server.community.domain.QArticleBookmark.articleBookmark;
import static com.adoptpet.server.community.domain.QArticleHeart.articleHeart;
import static com.adoptpet.server.community.domain.QCommunity.community;
import static com.adoptpet.server.community.domain.QCommunityImage.communityImage;
import static com.adoptpet.server.user.domain.QMember.member;
import static com.adoptpet.server.community.domain.QComment.comment;

@Slf4j
@Repository
public class CommunityQDslRepository {

    private final JPAQueryFactory query;

    public CommunityQDslRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    //== 인기 게시글 조회  ==//
    public List<TrendingArticleDto> findTrendingArticles(LocalDateTime startAt,
                                                         LocalDateTime endAt)
    {
        return query.select(Projections.constructor(TrendingArticleDto.class,
                    articleHeart.community.articleNo,
                    articleHeart.community.articleNo.count().as("likeCnt")
                ))
                .from(articleHeart)
                .where(articleHeart.regDate.between(startAt,endAt))
                .groupBy(articleHeart.community.articleNo)
                .fetch();
    }


    //== 게시글 상세내용 조회 ==//
    public ArticleDetailInfoDto findArticleDetail(Integer articleNo){
        return query
                .select(new QArticleDetailInfoDto(
                        community.articleNo,
                        community.title,
                        member.nickname,
                        member.memberNo,
                        member.profile,
                        community.viewCount.as("view"),
                        articleHeart.heartNo.countDistinct().intValue().as("like"),
                        comment.commentNo.countDistinct().intValue().as("comment"),
                        community.regDate,
                        community.content
                ))
                .from(community)
                .leftJoin(member).on(community.regId.eq(member.email))
                .leftJoin(articleHeart).on(community.articleNo.eq(articleHeart.community.articleNo))
                .leftJoin(comment).on(community.articleNo.eq(comment.community.articleNo))
                .where(community.articleNo.eq(articleNo))
                .groupBy(community.articleNo,community.regId,community.title,member.nickname,
                        member.memberNo,member.profile,community.regDate,community.content)
                .fetchFirst();
    }


    //== 게시글의 소유자인지 이메일로 검증 ==//
    public boolean isMine(String email, Integer articleNo) {
            return query.select(community.articleNo)
                    .from(community)
                    .where(community.regId.eq(email), community.articleNo.eq(articleNo))
                    .fetchFirst() != null;
    }


    //== 게시글 목록 조회 - 검색용 ==//
    public List<ArticleListDto> findArticleList(String order, Integer pageNum, Integer option, String keyword) {

        // row 시작 위치 (mysql에서는 limit)
        final Integer offset = pageNum == null? 0 : (pageNum * 10) - 10; // 1 -> 0, 2 -> 10 3 -> 20
        // row 개수
        final Integer limit = 10;

        // orderBy에서 like 서브쿼리의 alias를 읽기 위한 객체 생성
        StringPath likeAlias = Expressions.stringPath("likeCnt");

        BooleanExpression searchCondition = null;

        if(Objects.nonNull(option)){
            switch (option){
                case 1:
                    searchCondition = titleLike(keyword);
                    break;
                case 2:
                    searchCondition = contentLike(keyword);
                    break;
                case 3:
                    searchCondition = titleLikeOrContentLike(keyword);
                    break;
                default:
                    break;
            }
        }

        return findArticleListQuery()
                .where(searchCondition)
                .orderBy(order.equals("like") ? likeAlias.desc() : community.articleNo.desc())
                .offset(offset).limit(limit)
                .fetch();
    }


    //== 게시글 목록용 인기 게시글 데이터 조회 ==//
    public ArticleListDto findArticleOneForList(Integer articleNo){
        return findArticleListQuery()
                .where(community.articleNo.eq(articleNo))
                .fetchOne();
    }



    //== 게시글 목록 - 회원용 ==//
    public List<ArticleListDto> findArticleList(String email){
        return findArticleListQuery()
                .where(community.regId.eq(email))
                .orderBy(community.articleNo.desc())
                .fetch();
    }


    //== 게시글 목록 공통 쿼리 ==//
    private JPQLQuery<ArticleListDto> findArticleListQuery(){
        JPQLQuery<Integer> likeCntByCommunity = JPAExpressions.select(articleHeart.count().intValue())
                .from(articleHeart)
                .where(articleHeart.community.eq(community));

        JPQLQuery<Integer> commentCntByCommunity = JPAExpressions.select(comment.count().intValue())
                .from(comment)
                .where(comment.community.eq(community));

        return query.select(Projections.fields(ArticleListDto.class,
                        community.articleNo,
                        community.title,
                        community.content,
                        member.nickname,
                        community.viewCount,
                        Expressions.as(likeCntByCommunity,"likeCnt"),
                        Expressions.as(commentCntByCommunity,"commentCnt"),
                        community.regDate,
                        community.thumbnail
                ))
                .from(community)
                .leftJoin(member).on(community.regId.eq(member.email))
                .where(community.blindYn.eq(BlindEnum.NORMAL),
                        community.logicalDel.eq(LogicalDelEnum.NORMAL))
                .groupBy(community.articleNo,community.title,community.content,
                        member.nickname,community.viewCount,community.regDate,
                        community.thumbnail);
    }


    public List<ImageInfoDto> findImageUrlByArticleNo(Integer articleNo){

        return query.select(Projections.constructor(ImageInfoDto.class,
                        communityImage.pictureNo.as("ImageNo"),
                        communityImage.imageUrl
                ))
                .from(communityImage)
                .where(communityImage.articleNo.eq(articleNo))
                .orderBy(communityImage.sort.asc())
                .fetch();

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


    private BooleanExpression titleLike(String title) {
        return !StringUtils.hasText(title) ? null : community.title.contains(title);
    }

    private BooleanExpression contentLike(String content) {
        return !StringUtils.hasText(content) ? null : community.content.contains(content);
    }

    private BooleanExpression titleLikeOrContentLike(String keyword) {
        return !StringUtils.hasText(keyword) ? null : contentLike(keyword).or(titleLike(keyword));
    }
}
