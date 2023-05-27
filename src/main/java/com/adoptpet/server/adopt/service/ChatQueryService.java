package com.adoptpet.server.adopt.service;

import static com.adoptpet.server.adopt.domain.QChat.*;
import com.adoptpet.server.adopt.dto.response.ChatRoomResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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

    public List<ChatRoomResponseDto> getChattingList(Integer memberNo, Integer saleNo) {
        return jpaQueryFactory.select(Projections.constructor(ChatRoomResponseDto.class,
                chat.chatNo,
                chat.createMember,
                chat.joinMember,
                chat.saleNo,
                chat.regDate
                ))
                .from(chat)
                .where(chat.createMember.eq(memberNo).or(chat.joinMember.eq(memberNo)), saleNoEq(saleNo))
                .fetch();
    }

    private BooleanExpression saleNoEq(Integer saleNo) {
        return Objects.nonNull(saleNo) ? chat.saleNo.eq(saleNo) : null;
    }
}
