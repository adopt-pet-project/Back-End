package com.adoptpet.server;

import com.adoptpet.server.adopt.domain.mongo.Chatting;
import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.adopt.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;


@EnableJpaAuditing //AuditingEntityListener 사용 설정
@SpringBootApplication
@EnableScheduling // SpringScheduling 사용 설정
@RequiredArgsConstructor
public class BackEndApplication implements CommandLineRunner {
    private final MongoChatRepository mongoChatRepository;
    private final ChatRoomRepository chatRoomRepository;


    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        List<Chatting> chattings = mongoChatRepository.findByChatRoomNo(1);
//
//        for (Chatting chatting : chattings) {
//            System.out.println("chatting = " + chatting);
//        }
        mongoChatRepository.deleteAll();
//        chatRoomRepository.deleteAll();
    }
}
