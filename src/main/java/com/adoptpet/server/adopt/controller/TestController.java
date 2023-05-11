package com.adoptpet.server.adopt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {



    @GetMapping("/user")
    public String user() {
        log.info("user = {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "userPage!!";
    }

    @GetMapping("/admin")
    public String admin() {
        log.info("user = {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "adminPage!!";
    }


}
