package com.adoptpet.server.user.controller;

import com.adoptpet.server.adopt.dto.response.MyAdoptResponse;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.user.service.MyAdoptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MyAdoptController {

    private final MyAdoptService myAdoptService;

    @GetMapping("/mypage/adopt")
    public ResponseEntity<List<MyAdoptResponse>> myAdopt(@RequestParam("status") final String status) {
        List<MyAdoptResponse> myAdoptList = myAdoptService.myAdoptList(status, SecurityUtils.getUser());
        return ResponseEntity.ok(myAdoptList);
    }
}

