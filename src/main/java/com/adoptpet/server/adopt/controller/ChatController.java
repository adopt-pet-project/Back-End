package com.adoptpet.server.adopt.controller;

import com.adoptpet.server.adopt.domain.Chat;
import com.adoptpet.server.adopt.domain.mongo.Chatting;
import com.adoptpet.server.adopt.dto.chat.Message;
import com.adoptpet.server.adopt.dto.request.ChatRequestDto;
import com.adoptpet.server.adopt.repository.mongo.ChatMongoRepository;
import com.adoptpet.server.adopt.service.ChatService;
import com.adoptpet.server.commons.support.StatusResponseDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatMongoRepository chatMongoRepository;
    private final MemberService memberService;

    @PostMapping("/chatroom")
    public ResponseEntity<StatusResponseDto> createChatRoom(@RequestBody @Valid final ChatRequestDto requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(StatusResponseDto.addStatus(400));
        }

        // 채팅방을 만들어준다.
        chatService.makeChatRoom(SecurityUtils.getUser(), requestDto);

        return ResponseEntity.ok(StatusResponseDto.addStatus(200));
    }

    // 채팅내역 조회
    @GetMapping("/chatroom/{roomNo}")
    public ResponseEntity<List<Chatting>> chattingList(@PathVariable("roomNo") Integer roomNo) {
        List<Chatting> chattingList = chatMongoRepository.findByChatRoomNo(roomNo);
        return ResponseEntity.ok(chattingList);
    }

    // 채팅방 리스트 조회
    @GetMapping("/chatroom")
    public ResponseEntity<List<Chat>> chatRoomList() {
        List<Chat> chatList = chatService.getChatList(SecurityUtils.getUser());
        return ResponseEntity.ok(chatList);
    }

    @MessageMapping("/message")
    public void sendMessage(@Valid Message message, @Header("Authorization") final String accessToken) {

        log.info("Produce message : " + message.toString());
        chatService.sendMessage(message, accessToken);
    }
}
