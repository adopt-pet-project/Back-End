package com.adoptpet.server;

import com.adoptpet.server.adopt.dto.response.AdoptResponseDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableJpaAuditing //AuditingEntityListener 사용 설정
@SpringBootApplication
@EnableWebMvc
public class BackEndApplication {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }

}
