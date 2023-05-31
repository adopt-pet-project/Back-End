package com.adoptpet.server.adopt.service;

import static com.adoptpet.server.adopt.domain.QChat.*;

import com.adoptpet.server.adopt.domain.Chat;
import com.adoptpet.server.adopt.dto.response.ChatRoomResponseDto;
import static com.adoptpet.server.user.domain.QMember.*;

import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.repository.MemberRepository;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatQueryService {

    private final JPAQueryFactory jpaQueryFactory;
    private final MemberRepository memberRepository;

    // 채팅방 리스트 조회
    public List<ChatRoomResponseDto> getChattingList(Integer memberNo, Integer saleNo) {
        return jpaQueryFactory.select(Projections.constructor(ChatRoomResponseDto.class,
                chat.chatNo,
                chat.createMember,
                chat.joinMember,
                chat.saleNo,
                chat.regDate,
                Projections.constructor(ChatRoomResponseDto.Participant.class,
                        ExpressionUtils.as(
                                JPAExpressions.select(member.nickname)
                                        .from(member)
                                        .where(member.memberNo.eq(
                                                new CaseBuilder()
                                                        .when(chat.createMember.eq(memberNo)).then(chat.joinMember)
                                                        .otherwise(chat.createMember)

                                        ))
                        , "nickname"),
                        ExpressionUtils.as(
                                JPAExpressions.select(member.profile)
                                        .from(member)
                                        .where(member.memberNo.eq(
                                                new CaseBuilder()
                                                        .when(chat.createMember.eq(memberNo)).then(chat.joinMember)
                                                        .otherwise(chat.createMember)
                                        )), "profile"))
                ))
                .from(chat)
                .where(chat.createMember.eq(memberNo).or(chat.joinMember.eq(memberNo)), saleNoEq(saleNo))
                .fetch();
    }

    // 현재 메시지를 받는 사람을 조회하는 메서드
    public Member getReceiverNumber(Integer chatNo, Integer senderNo) {
        Chat chatroom = jpaQueryFactory.select(chat)
                .from(chat)
                .where(chat.chatNo.eq(chatNo))
                .fetchOne();

        Integer memberNo = chatroom.getCreateMember().equals(senderNo) ?
                chatroom.getJoinMember() : chatroom.getCreateMember();

        return memberRepository.findById(memberNo)
                .orElseThrow(IllegalStateException::new);
    }

    private BooleanExpression saleNoEq(Integer saleNo) {
        return Objects.nonNull(saleNo) ? chat.saleNo.eq(saleNo) : null;
    }

}
