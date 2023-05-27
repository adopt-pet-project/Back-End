package com.adoptpet.server.adopt.controller;

import com.adoptpet.server.adopt.dto.chat.Message;
import com.adoptpet.server.adopt.dto.request.ChatRequestDto;
import com.adoptpet.server.adopt.dto.response.ChatResponseDto;
import com.adoptpet.server.adopt.dto.response.ChatRoomResponseDto;
import com.adoptpet.server.adopt.service.ChatService;
import com.adoptpet.server.commons.support.StatusResponseDto;
import com.adoptpet.server.commons.util.SecurityUtils;
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
    public ResponseEntity<List<ChatResponseDto>> chattingList(@PathVariable("roomNo") Integer roomNo) {
        List<ChatResponseDto> chattingList = chatService.getChattingList(roomNo, SecurityUtils.getUser());
        return ResponseEntity.ok(chattingList);
    }

    // 채팅방 리스트 조회
    @GetMapping("/chatroom")
    public ResponseEntity<List<ChatRoomResponseDto>> chatRoomList(@RequestParam(value = "saleNo", required = false) final Integer saleNo) {
        List<ChatRoomResponseDto> chatList = chatService.getChatList(SecurityUtils.getUser(), saleNo);
        return ResponseEntity.ok(chatList);
    }

    @MessageMapping("/message")
    public void sendMessage(@Valid Message message, @Header("Authorization") final String accessToken) {

        log.info("Produce message : " + message.toString());
        chatService.sendMessage(message, accessToken);
    }

    // 채팅 카운트 읽음 처리
    @PostMapping("/chatroom/chat")
    public ResponseEntity<Message> readChat(@Valid @RequestBody Message message, @RequestHeader("Authorization") final String accessToken) {
        Message recievedMessage = chatService.updateCountToZero(message, accessToken);
        return ResponseEntity.ok(recievedMessage);
    }
}
