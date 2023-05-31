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

    // 메시지를 지정한 Kafka 토픽으로 전송
    public void send(String topic, Message data) {
        // KafkaTemplate을 사용하여 메시지를 지정된 토픽으로 전송

        log.info("메시지 전송 중... = {}", data);

        kafkaTemplate.send(topic, data);
    }
}
