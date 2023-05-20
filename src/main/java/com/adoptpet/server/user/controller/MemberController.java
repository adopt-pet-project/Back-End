package com.adoptpet.server.user.controller;

import com.adoptpet.server.commons.support.StatusResponseDto;
import com.adoptpet.server.user.dto.request.RegisterDto;
import com.adoptpet.server.user.dto.response.MemberResponseDto;
import com.adoptpet.server.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity<StatusResponseDto> register(@Valid @RequestBody RegisterDto registerDto, BindingResult bindingResult) {
        // DTO의 유효성 검사가 실패할 경우 400번 에러를 돌려준다.
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(StatusResponseDto.addStatus(400));
        }

        // 회원을 저장한다.
        memberService.save(registerDto);
        return ResponseEntity.ok(StatusResponseDto.addStatus(200));
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<MemberResponseDto> userInfo(@PathVariable(value = "id") final Integer memberNo) {
        MemberResponseDto responseDto = memberService.findMemberInfo(memberNo);

        return ResponseEntity.ok(responseDto);
    }
}
