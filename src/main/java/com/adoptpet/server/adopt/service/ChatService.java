package com.adoptpet.server.adopt.service;

import com.adoptpet.server.adopt.domain.Adopt;
import com.adoptpet.server.adopt.domain.Chat;
import com.adoptpet.server.adopt.domain.mongo.Chatting;
import com.adoptpet.server.adopt.dto.chat.Message;
import com.adoptpet.server.adopt.dto.request.ChatRequestDto;
import com.adoptpet.server.adopt.repository.ChatRepository;
import com.adoptpet.server.adopt.repository.mongo.ChatMongoRepository;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.commons.util.ConstantUtil;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final AdoptQueryService queryService;
    private final ChatMongoRepository chatMongoRepository;
    private final MessageSender sender;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Chat makeChatRoom(SecurityUserDto userDto, ChatRequestDto requestDto) {
        // 채팅을 걸려고 하는 분양글이 분양 가능 상태인지 조회해본다.
        Adopt saleAdopt = queryService.isAdopting(requestDto.getSaleNo());

        // 조회해온 분양글이 NULL 이라면 분양이 불가능한 상태이다.
        if (Objects.isNull(saleAdopt)) {
            throw new IllegalStateException("현재 분양가능 상태가 아닙니다.");
        }

        Chat chat = Chat.builder()
                .saleNo(requestDto.getSaleNo())
                .createMember(requestDto.getCreateMember())
                .joinMember(userDto.getMemberNo())
                .regDate(LocalDateTime.now())
                .build();

        return chatRepository.save(chat);

    }

    public List<Chat> getChatList(SecurityUserDto userDto) {
        return chatRepository.findChattingRoom(userDto.getMemberNo());
    }

    @Transactional
    public void sendMessage(Message message, String accessToken) {
        Member findMember = memberRepository.findByEmail(jwtUtil.getUid(accessToken))
                        .orElseThrow(IllegalStateException::new);
        message.setSendTimeAndSender(LocalDateTime.now(), findMember.getMemberNo(), findMember.getNickname());
        Chatting chatting = message.convertEntity();
        chatting.setDate(LocalDateTime.now());
        chatMongoRepository.save(chatting);
        sender.send(ConstantUtil.KAFKA_TOPIC, message);
    }


}
