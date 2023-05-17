package com.adoptpet.server.adopt.controller;

import com.adoptpet.server.adopt.dto.request.AdoptBookmarkRequestDto;
import com.adoptpet.server.adopt.dto.request.AdoptRequestDto;
import com.adoptpet.server.adopt.dto.response.AdoptResponseDto;
import com.adoptpet.server.adopt.service.AdoptService;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdoptController {

    private final AdoptService adoptService;

    @PostMapping("/adopt")
    public ResponseEntity<Void> writeAdopt(@RequestBody @Valid AdoptRequestDto adoptDto, BindingResult bindingResult) {

        // 유효성 검증에 실패할경우 400번 에러를 내려준다.
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        // 현재 회원의 인증 객체를 가져온다.
        SecurityUserDto user = SecurityUtils.getUser();

        // 새로운 분양글을 저장한다.
        adoptService.insertAdopt(adoptDto, user);

        return ResponseEntity.ok().build();

    }

    @PostMapping("/adopt/bookmark")
    public ResponseEntity<Void> addBookMark(@RequestBody @Valid AdoptBookmarkRequestDto requestDto, BindingResult bindingResult) {

        // requestDto 유효성 검사 실패시 400번 에러를 내려준다.
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        // 관심 분양 게시글 등록
        adoptService.insertAdoptBookmark(SecurityUtils.getUser(), requestDto.getSaleNo());

        return ResponseEntity.ok().build();
    }



    @GetMapping("/adopt/{saleNo}")
    public ResponseEntity<AdoptResponseDto> readAdopt(@PathVariable("saleNo") Integer saleNo) {
        AdoptResponseDto responseDto = adoptService.readAdopt(saleNo);
        return ResponseEntity.ok(responseDto);
    }
}
