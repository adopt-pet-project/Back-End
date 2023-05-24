package com.adoptpet.server.adopt.service;

import com.adoptpet.server.adopt.domain.Adopt;
import com.adoptpet.server.adopt.domain.Chat;
import com.adoptpet.server.adopt.domain.mongo.Chatting;
import com.adoptpet.server.adopt.dto.chat.Message;
import com.adoptpet.server.adopt.dto.request.ChatRequestDto;
import com.adoptpet.server.adopt.dto.response.ChatResponseDto;
import com.adoptpet.server.adopt.dto.response.ChatRoomResponseDto;
import com.adoptpet.server.adopt.repository.ChatRepository;
import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.commons.util.ConstantUtil;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final AdoptQueryService queryService;
    private final MongoChatRepository mongoChatRepository;
    private final MessageSender sender;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final ChatQueryService chatQueryService;

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

    public List<ChatRoomResponseDto> getChatList(SecurityUserDto userDto) {
        List<ChatRoomResponseDto> chatRoomList = chatQueryService.getChattingList(userDto.getMemberNo());
        List<Integer> chatNos = chatRoomList.stream()
                .map(ChatRoomResponseDto::getChatNo)
                .collect(Collectors.toList());
        mongoChatRepository.updateReadCountToOneByChatRoomNo(chatRoomList.get(0).getChatNo());

        List<Long> unReadCounts = mongoChatRepository.findUnreadChattingCount(chatNos, userDto.getMemberNo());

        log.info("chatRoomList = {}", chatRoomList);
        log.info("chatReadCounts = {}", unReadCounts);

        for (int i = 0; i<chatRoomList.size(); i++) {
            chatRoomList.get(i).setUnReadCount(unReadCounts.get(i));
        }

        return chatRoomList;
    }

    public List<ChatResponseDto> getChattingList(Integer chatRoomNo) {
        List<Chatting> chattingList = mongoChatRepository.findByChatRoomNo(chatRoomNo);

        return chattingList.stream()
                .map(ChatResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void sendMessage(Message message, String accessToken) {
        // 메시지 전송 요청 헤더에 포함된 Access Token에서 email로 회원을 조회한다.
        Member findMember = memberRepository.findByEmail(jwtUtil.getUid(accessToken))
                        .orElseThrow(IllegalStateException::new);
        // message 객체에 보낸시간, 보낸사람 memberNo, 닉네임을 셋팅해준다.
        message.setSendTimeAndSender(LocalDateTime.now(), findMember.getMemberNo(), findMember.getNickname());
        // Message 객체를 채팅 엔티티로 변환한다.
        Chatting chatting = message.convertEntity();
        // 채팅 내용을 저장한다.
        mongoChatRepository.save(chatting);
        // 메시지를 전송한다.
        sender.send(ConstantUtil.KAFKA_TOPIC, message);
    }


}
