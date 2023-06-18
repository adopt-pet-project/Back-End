package com.adoptpet.server;

import com.adoptpet.server.adopt.domain.mongo.Chatting;
import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.adopt.repository.ChatRepository;
import com.adoptpet.server.adopt.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;


@EnableJpaAuditing //AuditingEntityListener 사용 설정
@SpringBootApplication
@EnableScheduling // SpringScheduling 사용 설정
@RequiredArgsConstructor
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class PetHubApplication implements CommandLineRunner {

    private final MongoChatRepository chatRepository;

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    public static void main(String[] args) {
        SpringApplication.run(PetHubApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
