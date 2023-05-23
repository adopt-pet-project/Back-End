package com.adoptpet.server.adopt.service;

import com.adoptpet.server.adopt.dto.chat.Message;
import com.adoptpet.server.commons.util.ConstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageReceiver {

    private final SimpMessagingTemplate template;

    @KafkaListener(topics = ConstantUtil.KAFKA_TOPIC)
    public void receiveMessage(Message message) {
        log.info("message = {}", message);
        template.convertAndSend("/subscribe/public/" + message.getChatNo(), message);
    }
}
