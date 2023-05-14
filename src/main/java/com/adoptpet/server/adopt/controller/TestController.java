package com.adoptpet.server.adopt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class TestController {


    @GetMapping("/")
    public String main() {
        return "main page!";
    }

    @GetMapping("/user")
    public String user() {
        log.info("user = {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "userPage!!";
    }

    @PutMapping("/user")
    public String put() {
        return "putUser";
    }

    @PostMapping("/user")
    public String post() {
        return "postUser";
    }

    @DeleteMapping("/user")
    public String delete() {
        return "deleteUser";
    }

    @GetMapping("/admin")
    public String admin() {
        log.info("user = {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "adminPage!!";
    }


}
