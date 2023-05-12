package com.adoptpet.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing //AuditingEntityListener 사용 설정
@SpringBootApplication
public class BackEndApplication {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    public static void main(String[] args) {

        SpringApplication.run(BackEndApplication.class, args);
    }

}
