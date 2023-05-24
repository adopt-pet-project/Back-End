package com.adoptpet.server.adopt.service;

import static com.adoptpet.server.adopt.domain.QAdoptImage.*;
import static com.adoptpet.server.adopt.domain.QAdopt.*;
import static com.adoptpet.server.adopt.domain.QAdoptBookmark.*;
import static com.adoptpet.server.adopt.domain.QChat.*;
import com.adoptpet.server.adopt.domain.Adopt;
import com.adoptpet.server.adopt.domain.AdoptStatus;
import com.adoptpet.server.adopt.dto.response.AdoptDetailResponseDto;
import static com.adoptpet.server.user.domain.QMember.*;
import static com.adoptpet.server.user.domain.QProfileImage.*;

import com.adoptpet.server.adopt.dto.response.AdoptImageResponseDto;
import com.adoptpet.server.adopt.dto.response.AdoptResponseDto;
import com.adoptpet.server.adopt.dto.response.MyAdoptResponse;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdoptQueryService {
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;


    // 분양 게시글과 관련있는 이미지를 조회하는 메서드
    public List<AdoptImageResponseDto> selectAdoptImages(Integer saleNo) {
        return jpaQueryFactory.select(Projections.constructor(AdoptImageResponseDto.class,
                        adoptImage.pictureNo,
                        adoptImage.imageUrl
                        ))
                .from(adoptImage)
                .where(adoptImage.saleNo.eq(saleNo))
                .orderBy(adoptImage.sort.asc())
                .fetch();
    }

    // 분양 게시글 소유자인지 확인
    public boolean isMine(String email, Integer saleNo) {
        return jpaQueryFactory.select(adopt.saleNo)
                .from(adopt)
                .where(adopt.regId.eq(email), adopt.saleNo.eq(saleNo))
                .fetchFirst() != null;
    }

    // 분양 게시글의 대표 이미지를 조회하는 메서드
    public String selectAdoptImage(Integer saleNo) {
        return jpaQueryFactory.select(adoptImage.imageUrl)
                .from(adoptImage)
                .where(adoptImage.saleNo.eq(saleNo))
                .orderBy(adoptImage.sort.asc())
                .limit(1)
                .fetchOne();
    }

    // 분양 게시글 리스트 조회
    public List<AdoptResponseDto> selectAdoptList(Integer saleNo, String keyword, Integer option, String filter) {

        BooleanExpression searchCondition = null;
        // option이 null이 아닐경우 검색 조건에 따라 쿼리를 동적으로 변경한다.
        if (Objects.nonNull(option)) {
            switch (option) {
                case 1:
                    searchCondition = titleLike(keyword);
                    break;
                case 2:
                    searchCondition = contentLike(keyword);
                    break;
                case 3:
                    searchCondition = titleLikeOrContentLike(keyword);
                    break;
                case 4:
                    searchCondition = speciesLike(keyword);
                    break;
                default:
                    break;
            }
        }

        return jpaQueryFactory
                .select(Projections.constructor(AdoptResponseDto.class,
                        adopt.saleNo,
                        adopt.title,
                        adopt.address,
                        // select subQuery를 이용하여 집계함수를 사용
                        ExpressionUtils.as(
                                JPAExpressions.select(adoptBookmark.count())
                                        .from(adoptBookmark)
                                        .where(adoptBookmark.adopt.saleNo.eq(adopt.saleNo))
                                , "bookmark"
                        ),
                        // select subQuery를 이용하여 집계함수를 사용
                        ExpressionUtils.as(
                                JPAExpressions.select(chat.count())
                                        .from(chat)
                                        .where(chat.saleNo.eq(adopt.saleNo))
                                , "chat"
                        ),
                        adopt.regDate,
                        adopt.thumbnail,
                        adopt.species,
                        adopt.status
                                .when(AdoptStatus.ADOPTING).then(0)
                                .when(AdoptStatus.RESERVED).then(1)
                                .when(AdoptStatus.END).then(2)
                                .when(AdoptStatus.CANCEL).then(3)
                                .otherwise(9)
                        ))
                .from(adopt)
                .orderBy(adopt.saleNo.desc())
                .innerJoin(member).on(adopt.regId.eq(member.email))
                .where(saleNoLt(saleNo), searchCondition, kindLike(filter))
                .limit(10)
                .fetch();
    }

    // 분양글과 관계가 이미지 리스트를 지우는 메서드
    @Transactional
    public void deleteAdoptImages(Integer saleNo) {
        jpaQueryFactory.delete(adoptImage)
                .where(adoptImage.saleNo.eq(saleNo))
                .execute();

        em.flush();
        em.clear();
    }

    // 현재 분양글이 분양 상태인지 조회하는 메서드
    public Adopt isAdopting(Integer saleNo) {
        return jpaQueryFactory.selectFrom(adopt)
                .where(adopt.saleNo.eq(saleNo), adopt.status.eq(AdoptStatus.ADOPTING))
                .fetchOne();
    }

    // 분양글과 관계가 있는 북마크를 지우는 메서드
    @Transactional
    public void removeBookmark(Integer saleNo) {
        jpaQueryFactory.delete(adoptBookmark)
                .where(adoptBookmark.adopt.saleNo.eq(saleNo))
                .execute();

        em.flush();
        em.clear();
    }


    // 분양 테이블과 회원 테이블을 조인해서 정보를 가져오는 메서드
    public AdoptDetailResponseDto selectAdoptAndMember(Integer saleNo) {
                // AdoptResponseDto의 생성자를 이용해서 값을 반환 받는다.
        return jpaQueryFactory.select(Projections.constructor(AdoptDetailResponseDto.class,
                            adopt.saleNo,
                    // AdoptResponseDto의 내부 클래스의 값은 중첩 생성자 Projection으로 채워준다.
                    Projections.constructor(AdoptDetailResponseDto.Header.class,
                            adopt.title,
                            adopt.status
                                    .when(AdoptStatus.ADOPTING).then(0)
                                    .when(AdoptStatus.RESERVED).then(1)
                                    .when(AdoptStatus.END).then(2)
                                    .when(AdoptStatus.CANCEL).then(3)
                                    .otherwise(9),
                            adopt.regDate
                    ),
                    Projections.constructor(AdoptDetailResponseDto.Metadata.class,
                            adopt.gender,
                            adopt.age,
                            adopt.name,
                            adopt.species
                    ),
                    Projections.constructor(AdoptDetailResponseDto.Context.class,
                            adopt.content,
                            // select subQuery를 이용하여 집계함수를 사용
                            ExpressionUtils.as(
                                    JPAExpressions.select(adoptBookmark.count())
                                            .from(adoptBookmark)
                                            .where(adoptBookmark.adopt.saleNo.eq(saleNo))
                                            , "bookmark"
                            ),
                            ExpressionUtils.as(
                                    JPAExpressions.select(chat.count())
                                            .from(chat)
                                            .where(chat.saleNo.eq(saleNo))
                                    , "chat"
                            )
                    ),
                    Projections.constructor(AdoptDetailResponseDto.Author.class,
                            member.memberNo,
                            member.nickname,
                            profileImage.imageUrl,
                            member.address
                    ),
                    Projections.constructor(AdoptDetailResponseDto.Coords.class,
                            adopt.latitude,
                            adopt.longitude,
                            adopt.address
                    )

                ))
                .from(adopt)
                .join(member).on(adopt.regId.eq(member.email))
                .join(profileImage).on(profileImage.memberNo.eq(member.memberNo))
                .where(adopt.saleNo.eq(saleNo))
                .fetchOne();

    }

    // 상태값 별 마이페이지 분양 리스트 반환
    public List<MyAdoptResponse> myAdoptList(String status, SecurityUserDto userDto) {
        return jpaQueryFactory.select(Projections.constructor(MyAdoptResponse.class,
                adopt.saleNo,
                adopt.title,
                adopt.content,
                member.nickname,
                adopt.viewCnt,
                ExpressionUtils.as(
                        JPAExpressions.select(adoptBookmark.count())
                                .from(adoptBookmark)
                                .where(adoptBookmark.adopt.saleNo.eq(adopt.saleNo))
                        , "like"
                ),
                adopt.regDate,
                adopt.thumbnail
        ))
                .from(adopt)
                .join(member).on(adopt.regId.eq(member.email))
                .where(adopt.regId.eq(userDto.getEmail()), adopt.status.eq(AdoptStatus.valueOf(status.toUpperCase())))
                .fetch();
    }

    // 나의 관심 분양 글 조회
    public List<MyAdoptResponse> myInterestedAdoptList(List<Integer> keys) {
        return jpaQueryFactory.select(Projections.constructor(MyAdoptResponse.class,
                adopt.saleNo,
                adopt.title,
                adopt.content,
                member.nickname,
                adopt.viewCnt,
                ExpressionUtils.as(
                        JPAExpressions.select(adoptBookmark.count())
                                .from(adoptBookmark)
                                .where(adoptBookmark.adopt.saleNo.eq(adopt.saleNo))
                        , "like"
                ),
                adopt.regDate,
                adopt.thumbnail
                ))
                .from(adopt)
                .join(member).on(adopt.regId.eq(member.email))
                .where(adopt.saleNo.in(keys))
                .fetch();
    }

    // 나의 관심 분양 글의 PK List 조회
    public List<Integer> myInterestedAdoptKeys(SecurityUserDto userDto) {
        return jpaQueryFactory.select(adoptBookmark.adopt.saleNo)
                .from(adoptBookmark)
                .where(adoptBookmark.member.memberNo.eq(userDto.getMemberNo()))
                .fetch();
    }

    /*
    *   BooleanExpression을 사용하여 검색 조건과 기타 조건들을 조합해서 사용할 수 있는
    *   Composition을 사용한다.
    * */
    private BooleanExpression saleNoLt(Integer saleNo) {
        return Objects.isNull(saleNo) ? null : adopt.saleNo.lt(saleNo);
    }

    private BooleanExpression contentLike(String content) {
        return !StringUtils.hasText(content) ? null : adopt.content.contains(content);
    }

    private BooleanExpression titleLike(String title) {
        return !StringUtils.hasText(title) ? null : adopt.title.contains(title);
    }

    private BooleanExpression titleLikeOrContentLike(String keyword) {
        return !StringUtils.hasText(keyword) ? null : contentLike(keyword).or(titleLike(keyword));
    }

    private BooleanExpression kindLike(String kind) {
        return !StringUtils.hasText(kind) ? null : adopt.kind.contains(kind);
    }

    private BooleanExpression speciesLike(String species) {
        return !StringUtils.hasText(species) ? null : adopt.species.contains(species);
    }

}
