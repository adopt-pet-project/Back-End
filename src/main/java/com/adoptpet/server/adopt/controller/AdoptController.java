package com.adoptpet.server.adopt.controller;

import com.adoptpet.server.adopt.dto.request.AdoptRequestDto;
import com.adoptpet.server.adopt.dto.request.AdoptStatusRequestDto;
import com.adoptpet.server.adopt.dto.request.AdoptUpdateRequestDto;
import com.adoptpet.server.adopt.dto.response.AdoptDetailResponseDto;
import com.adoptpet.server.adopt.dto.response.AdoptResponseDto;
import com.adoptpet.server.adopt.service.AdoptQueryService;
import com.adoptpet.server.adopt.service.AdoptService;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.support.StatusResponseDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public ResponseEntity<StatusResponseDto> writeAdopt(@RequestBody @Valid AdoptRequestDto adoptDto, BindingResult bindingResult) {

        // 유효성 검증에 실패할경우 400번 에러를 내려준다.
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(StatusResponseDto.addStatus(400));
        }

        // 현재 회원의 인증 객체를 가져온다.
        SecurityUserDto user = SecurityUtils.getUser();

        // 새로운 분양글을 저장한다.
        adoptService.insertAdopt(adoptDto, user);


        return ResponseEntity.ok(StatusResponseDto.addStatus(200));

    }

    // 관심 분양 게시글 등록
    @PostMapping("/adopt/bookmark/{saleNo}")
    public ResponseEntity<StatusResponseDto> addBookMark(@PathVariable("saleNo") Integer saleNo) {

        // 관심 분양 게시글 등록
        adoptService.insertAdoptBookmark(SecurityUtils.getUser(), saleNo);

        return ResponseEntity.ok(StatusResponseDto.addStatus(200));
    }

    // 분양글 삭제
    @DeleteMapping("/adopt/{saleNo}")
    public ResponseEntity<StatusResponseDto> deleteAdopt(@PathVariable("saleNo") Integer saleNo) {
        adoptService.deleteAdopt(saleNo, SecurityUtils.getUser());
        return ResponseEntity.ok(StatusResponseDto.addStatus(200));
    }

    // 분양글 리스트 조회
    @GetMapping("/adopt")
    public ResponseEntity<List<AdoptResponseDto>> getAdoptList(@RequestParam(value = "saleNo", required = false) final Integer saleNo,
                                                               @RequestParam(value = "keyword", required = false) final String keyword,
                                                               @RequestParam(value = "option", required = false) final Integer option,
                                                               @RequestParam(value = "filter", required = false) final String filter) {
        List<AdoptResponseDto> adoptList = adoptQueryService.selectAdoptList(saleNo, keyword, option, filter);
        return ResponseEntity.ok(adoptList);
    }

    // 분양글 수정
    @PatchMapping("/adopt/{saleNo}")
    public ResponseEntity<StatusResponseDto> updateAdopt(@RequestBody @Valid AdoptUpdateRequestDto adoptDto, BindingResult bindingResult,
                                                         @PathVariable("saleNo") Integer saleNo) {
        // 유효성 검증에 실패할경우 400번 에러를 내려준다.
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(StatusResponseDto.addStatus(400));
        }

        // 현재 회원의 인증 객체를 가져온다.
        SecurityUserDto user = SecurityUtils.getUser();

        // 새로운 분양글을 저장한다.
        adoptService.updateAdopt(adoptDto, user, saleNo);

        return ResponseEntity.ok().body(StatusResponseDto.addStatus(200));
    }



    // 분양글 상세 조회
    @GetMapping("/adopt/{saleNo}")
    public ResponseEntity<AdoptDetailResponseDto> readAdopt(@PathVariable("saleNo") final Integer saleNo,
                                                            @RequestHeader(value = "Authorization", required = false) final String accessToken,
                                                            HttpServletRequest request, HttpServletResponse response) {
        AdoptDetailResponseDto responseDto = adoptService.readAdopt(saleNo, accessToken, request, response);
        return ResponseEntity.ok(responseDto);
    }

    // 분양글 상태 수정
    @PatchMapping("/adopt")
    public ResponseEntity<StatusResponseDto> updateStatus(@RequestBody final AdoptStatusRequestDto requestDto) {
        adoptService.updateAdoptStatus(requestDto);
        return ResponseEntity.ok(StatusResponseDto.addStatus(200));
    }
}
