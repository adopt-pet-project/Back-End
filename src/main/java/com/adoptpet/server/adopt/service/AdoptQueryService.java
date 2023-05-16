package com.adoptpet.server.adopt.service;

import static com.adoptpet.server.adopt.domain.QAdoptImage.*;

import static com.adoptpet.server.adopt.domain.QAdopt.*;

import static com.adoptpet.server.adopt.domain.QAdoptBookmark.*;
import com.adoptpet.server.adopt.dto.response.AdoptResponseDto;
import static com.adoptpet.server.user.domain.QMember.*;

import static com.adoptpet.server.user.domain.QProfileImage.*;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdoptQueryService {
    private final JPAQueryFactory jpaQueryFactory;


    // 분양 게시글과 관련있는 이미지를 조회하는 메서드
    public List<String> selectAdoptImages(Integer saleNo) {
        return jpaQueryFactory.select(adoptImage.imageUrl)
                .from(adoptImage)
                .where(adoptImage.saleNo.eq(saleNo))
                .fetch();
    }

    // 분양 테이블과 회원 테이블을 조인해서 정보를 가져오는 메서드
    public AdoptResponseDto selectAdoptAndMember(Integer saleNo) {
                // AdoptResponseDto의 생성자를 이용해서 값을 반환 받는다.
        return jpaQueryFactory.select(Projections.constructor(AdoptResponseDto.class,
                            adopt.saleNo,
                            adopt.regId,
                    // AdoptResponseDto의 내부 클래스의 값은 중첩 생성자 Projection으로 채워준다.
                    Projections.constructor(AdoptResponseDto.Header.class,
                            adopt.title,
                            adopt.status,
                            adopt.regDate
                    ),
                    Projections.constructor(AdoptResponseDto.Metadata.class,
                            adopt.gender,
                            adopt.age,
                            adopt.name,
                            adopt.kind
                    ),
                    Projections.constructor(AdoptResponseDto.Context.class,
                            adopt.content,
                            // select subQuery를 이용하여 집계함수를 사용
                            ExpressionUtils.as(
                                    JPAExpressions.select(adoptBookmark.count())
                                            .from(adoptBookmark)
                                            .where(adoptBookmark.adopt.saleNo.eq(saleNo))
                                            , "bookmark"
                            ),
                            ExpressionUtils.as(
                                    JPAExpressions.select(adoptBookmark.count())
                                            .from(adoptBookmark)
                                            .where(adoptBookmark.adopt.saleNo.eq(saleNo))
                                    , "chat"
                            )
                    ),
                    Projections.constructor(AdoptResponseDto.Author.class,
                            member.nickname,
                            profileImage.imageUrl,
                            member.address
                    )

                ))
                .from(adopt)
                .join(member).on(adopt.regId.eq(member.email))
                .join(profileImage).on(profileImage.memberNo.eq(member.memberNo))
                .where(adopt.saleNo.eq(saleNo))
                .fetchOne();

    }

}
