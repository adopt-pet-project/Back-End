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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
    private final MongoTemplate mongoTemplate;
    private final ChatRoomService chatRoomService;

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

    public List<ChatRoomResponseDto> getChatList(SecurityUserDto userDto, Integer saleNo) {
        List<ChatRoomResponseDto> chatRoomList = chatQueryService.getChattingList(userDto.getMemberNo(), saleNo);

        // 채팅방별로 읽지 않은 횟수를 셋팅
        for (ChatRoomResponseDto chat : chatRoomList) {
            long unReadCount = countUnReadMessages(chat.getChatNo(), userDto.getMemberNo());
            chat.setUnReadCount(unReadCount);
        }

        // 채팅방별로 마지막 채팅내용과 시간을 셋팅
        chatRoomList
                .forEach(chatRoomDto -> {
                    Page<Chatting> chatting =
                            mongoChatRepository.findByChatRoomNoOrderBySendDateDesc(chatRoomDto.getChatNo(), PageRequest.of(0, 1));
                    if (chatting.hasContent()) {
                        Chatting chat = chatting.getContent().get(0);
                        ChatRoomResponseDto.LatestMessage latestMessage = ChatRoomResponseDto.LatestMessage.builder()
                                .context(chat.getContent())
                                .sendAt(chat.getSendDate())
                                .build();
                        chatRoomDto.setLatestMessage(latestMessage);
                    }
                });

        return chatRoomList;
    }

    public List<ChatResponseDto> getChattingList(Integer chatRoomNo, SecurityUserDto user) {
        updateCountAllZero(chatRoomNo, user.getEmail());
        List<Chatting> chattingList = mongoChatRepository.findByChatRoomNo(chatRoomNo);

        return chattingList.stream()
                .map(chat -> new ChatResponseDto(chat, user.getMemberNo()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void sendMessage(Message message, String accessToken) {
        // 메시지 전송 요청 헤더에 포함된 Access Token에서 email로 회원을 조회한다.
        Member findMember = memberRepository.findByEmail(jwtUtil.getUid(accessToken))
                        .orElseThrow(IllegalStateException::new);

        // 채팅방에 모든 유저가 참여중인지 확인한다.
        boolean isConnectedAll = chatRoomService.isAllConnected(message.getChatNo());
        // 1:1 채팅이므로 2명 접속시 readCount 0, 한명 접속시 1
        Integer readCount = isConnectedAll ? 0 : 1;
        // message 객체에 보낸시간, 보낸사람 memberNo, 닉네임을 셋팅해준다.
        message.setSendTimeAndSender(LocalDateTime.now(), findMember.getMemberNo(), findMember.getNickname(), readCount);
        // Message 객체를 채팅 엔티티로 변환한다.
        Chatting chatting = message.convertEntity();
        // 채팅 내용을 저장한다.
        Chatting savedChat = mongoChatRepository.save(chatting);
        // 저장된 고유 ID를 반환한다.
        message.setId(savedChat.getId());
        log.info("Message = {}", message);
        // 메시지를 전송한다.
        sender.send(ConstantUtil.KAFKA_TOPIC, message);
    }

    public void updateMessage(String email) {
        Message message = Message.builder()
                .content("notice")
                .content(email + " 님이 돌아오셨습니다.")
                .build();

        sender.send(ConstantUtil.KAFKA_TOPIC, message);
    }

    // 현재 보낸 메시지가 상대방이 접속 중이라면 메시지 상태를 읽음 상태로 업데이트
    public Message updateCountToZero(Message message, String accessToken) {
        Member findMember = memberRepository.findByEmail(jwtUtil.getUid(accessToken))
                .orElseThrow(IllegalStateException::new);

        if (!message.getSenderNo().equals(findMember.getMemberNo())) {
            Update update = new Update().set("readCount", 0);
            Query query = new Query(Criteria.where("_id").is(message.getId()));

            mongoTemplate.updateMulti(query, update, Chatting.class);
        }

        return message;
    }

    // 읽지 않은 메시지 채팅장 입장시 읽음 처리
    public void updateCountAllZero(Integer chatNo, String email) {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(IllegalStateException::new);

        Update update = new Update().set("readCount", 0);
        Query query = new Query(Criteria.where("chatRoomNo").is(chatNo)
                .and("senderNo").ne(findMember.getMemberNo()));

        mongoTemplate.updateMulti(query, update, Chatting.class);
    }

    // 읽지 않은 메시지 카운트
    long countUnReadMessages(Integer chatRoomNo, Integer senderNo) {
        Query query = new Query(Criteria.where("chatRoomNo").is(chatRoomNo)
                .and("readCount").is(1)
                .and("senderNo").ne(senderNo));

        return mongoTemplate.count(query, Chatting.class);
    }


}
