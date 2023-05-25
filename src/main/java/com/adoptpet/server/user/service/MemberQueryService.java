package com.adoptpet.server.user.service;

import static com.adoptpet.server.user.domain.QMember.*;

import static com.adoptpet.server.user.domain.QProfileImage.*;
import static com.adoptpet.server.community.domain.QComment.*;
import static com.adoptpet.server.community.domain.QCommunity.*;
import static com.adoptpet.server.user.domain.QMemberDenied.*;
import com.adoptpet.server.user.dto.response.MemberResponseDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final JPAQueryFactory jpaQueryFactory;


    // 회원의 정보를 조회하는 메서드
    public MemberResponseDto getUserInfo(Integer memberNo) {
        return jpaQueryFactory
                .select(Projections.constructor(MemberResponseDto.class,
                    member.memberNo,
                    member.profile,
                    member.nickname,
                    member.address,
                    Projections.constructor(MemberResponseDto.Activity.class,
                            // 회원 작성 글수 카운팅
                            ExpressionUtils.as(
                                    JPAExpressions.select(community.count())
                                            .from(community)
                                            .where(community.regId.eq(member.email)),
                                    "document"
                            ),
                            // 회원 작성 댓글수 카운팅
                            ExpressionUtils.as(
                                    JPAExpressions.select(comment.count())
                                            .from(comment)
                                            .where(comment.regId.eq(member.email)),
                                    "comment"
                            ),
                            // 회원 정지 누적 수 카운팅
                            ExpressionUtils.as(
                                    JPAExpressions.select(memberDenied.count())
                                            .from(memberDenied)
                                            .where(memberDenied.memberNo.eq(memberNo)),
                                    "sanction"
                            )
                    )
                ))
                .from(member)
                .where(member.memberNo.eq(memberNo))
                .fetchOne();
    }

}
