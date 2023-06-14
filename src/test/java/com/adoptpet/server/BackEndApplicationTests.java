package com.adoptpet.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
class BackEndApplicationTests {
    public static void main(String[] args) {
        SpringApplication.run(PetHubApplication.class, args);
    }

//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Test
//    void contextLoads() {
//        memberRepository.findAll();
//    }

}
