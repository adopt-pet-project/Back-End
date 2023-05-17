package com.adoptpet.server.adopt.controller;

import com.adoptpet.server.adopt.dto.request.AdoptRequestDto;
import com.adoptpet.server.adopt.dto.response.AdoptDetailResponseDto;
import com.adoptpet.server.adopt.dto.response.AdoptResponseDto;
import com.adoptpet.server.adopt.service.AdoptQueryService;
import com.adoptpet.server.adopt.service.AdoptService;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdoptController {

    private final AdoptService adoptService;
    private final AdoptQueryService adoptQueryService;

    // 분양글 등록
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

    // 관심 분양 게시글 등록
    @PostMapping("/adopt/bookmark/{saleNo}")
    public ResponseEntity<Void> addBookMark(@PathVariable("saleNo") Integer saleNo) {

        // 관심 분양 게시글 등록
        adoptService.insertAdoptBookmark(SecurityUtils.getUser(), saleNo);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/adopt/{saleNo}")
    public ResponseEntity<Void> deleteAdopt(@PathVariable("saleNo") Integer saleNo) {
        adoptService.deleteAdopt(saleNo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/adopt")
    public ResponseEntity<List<AdoptResponseDto>> getAdoptList(@RequestParam(value = "saleNo", required = false) Integer saleNo,
                                                               @RequestParam(value = "keyword", required = false) String keyword,
                                                               @RequestParam(value = "option", required = false) Integer option) {
        List<AdoptResponseDto> adoptList = adoptQueryService.selectAdoptList(saleNo, keyword, option);
        return ResponseEntity.ok(adoptList);
    }

    @PatchMapping("/adopt/{saleNo}")
    public ResponseEntity<Void> updateAdopt(@RequestBody @Valid AdoptRequestDto adoptDto, BindingResult bindingResult,
                                            @PathVariable("saleNo") Integer saleNo) {
        // 유효성 검증에 실패할경우 400번 에러를 내려준다.
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        // 현재 회원의 인증 객체를 가져온다.
        SecurityUserDto user = SecurityUtils.getUser();

        // 새로운 분양글을 저장한다.
        adoptService.updateAdopt(adoptDto, user, saleNo);

        return ResponseEntity.ok().build();
    }



    @GetMapping("/adopt/{saleNo}")
    public ResponseEntity<AdoptDetailResponseDto> readAdopt(@PathVariable("saleNo") Integer saleNo) {
        AdoptDetailResponseDto responseDto = adoptService.readAdopt(saleNo);
        return ResponseEntity.ok(responseDto);
    }
}
