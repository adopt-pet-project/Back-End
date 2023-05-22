package com.adoptpet.server.commons.config.kafka;

import com.adoptpet.server.adopt.dto.chat.Message;
import com.adoptpet.server.commons.util.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MessageListener {

    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(
            topics = ConstantUtil.KAFKA_TOPIC,
            groupId = ConstantUtil.GROUP_ID
    )
    public void listen(Message message) {
        log.info("sending message");
        message.setSendTime(LocalDateTime.now());
        template.convertAndSend("/topic/group", message);
    }

}
