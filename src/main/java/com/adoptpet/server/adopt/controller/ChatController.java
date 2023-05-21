package com.adoptpet.server.adopt.controller;

import com.adoptpet.server.adopt.dto.request.ChatRequestDto;
import com.adoptpet.server.adopt.service.ChatService;
import com.adoptpet.server.commons.support.StatusResponseDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<StatusResponseDto> createChatRoom(@RequestBody @Valid final ChatRequestDto requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(StatusResponseDto.addStatus(400));
        }

        // 채팅방을 만들어준다.
        chatService.makeChatRoom(SecurityUtils.getUser(), requestDto);

        return ResponseEntity.ok(StatusResponseDto.addStatus(200));
    }
}
