package com.adoptpet.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableJpaAuditing //AuditingEntityListener 사용 설정
@SpringBootApplication
@EnableScheduling // SpringScheduling 사용 설정
public class BackEndApplication {


    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }

}
