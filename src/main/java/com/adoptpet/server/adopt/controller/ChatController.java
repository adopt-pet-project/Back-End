package com.adoptpet.server.adopt.controller;

import com.adoptpet.server.adopt.domain.mongo.Chatting;
import com.adoptpet.server.adopt.dto.chat.Message;
import com.adoptpet.server.adopt.dto.request.ChatRequestDto;
import com.adoptpet.server.adopt.repository.mongo.ChatMongoRepository;
import com.adoptpet.server.adopt.service.ChatService;
import com.adoptpet.server.commons.support.StatusResponseDto;
import com.adoptpet.server.commons.util.ConstantUtil;
import com.adoptpet.server.commons.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final ChatMongoRepository chatMongoRepository;

    @PostMapping("/chat")
    public ResponseEntity<StatusResponseDto> createChatRoom(@RequestBody @Valid final ChatRequestDto requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(StatusResponseDto.addStatus(400));
        }

        // 채팅방을 만들어준다.
        chatService.makeChatRoom(SecurityUtils.getUser(), requestDto);

        return ResponseEntity.ok(StatusResponseDto.addStatus(200));
    }

    @PostMapping("/publish")
    public void sendMessage(@RequestBody Message message) {
        log.info("Produce message : " + message.toString());
        Chatting chatting = message.convertEntity();
        chatting.setSendDateAndSender(SecurityUtils.getUser().getMemberNo(), LocalDateTime.now());
        chatMongoRepository.save(chatting);
        kafkaTemplate.send(ConstantUtil.KAFKA_TOPIC, message);
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/group")
    public Message broadcastGroupMessage(@Payload Message message) {
        return message;
    }
}
