package com.adoptpet.server.user.controller;

import com.adoptpet.server.user.dto.request.RegisterDto;
import com.adoptpet.server.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto registerDto, BindingResult bindingResult) {
        // DTO의 유효성 검사가 실패할 경우 400번 에러를 돌려준다.
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        // 회원을 저장한다.
        memberService.save(registerDto);
        return ResponseEntity.ok().build();
    }
}
