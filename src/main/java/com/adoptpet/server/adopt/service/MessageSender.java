package com.adoptpet.server.adopt.service;

import com.adoptpet.server.adopt.dto.chat.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSender {

    private final KafkaTemplate<String, Message> kafkaTemplate;

    public void send(String topic, Message data) {
        log.info("sendingDat = {} to topic={}", data, topic);
        kafkaTemplate.send(topic, data);
    }
}
