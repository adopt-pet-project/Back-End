package com.adoptpet.server.adopt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {



    @GetMapping("/user")
    public String user() {
        return "userPage!!";
    }

    @GetMapping("/login/oauth/code/google")
    public ResponseEntity<Void> auth() {
        return ResponseEntity.ok().build();
    }

}
