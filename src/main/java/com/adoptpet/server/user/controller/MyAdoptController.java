package com.adoptpet.server.user.controller;

import com.adoptpet.server.adopt.dto.response.MyAdoptResponseDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.community.dto.ArticleListDto;
import com.adoptpet.server.user.dto.response.MyArticleResponseDto;
import com.adoptpet.server.user.dto.response.MyCommentResponseDto;
import com.adoptpet.server.user.service.MyAdoptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyAdoptController {

    private final MyAdoptService myAdoptService;

    @GetMapping("/adopt")
    public ResponseEntity<List<MyAdoptResponseDto>> myAdopt(@RequestParam("status") final String status) {
        List<MyAdoptResponseDto> myAdoptList = myAdoptService.myAdoptList(status, SecurityUtils.getUser());
        return ResponseEntity.ok(myAdoptList);
    }


    @GetMapping("/article")
    public ResponseEntity<List<MyArticleResponseDto>> myArticle(){
        return ResponseEntity.ok(myAdoptService.myArticleList(SecurityUtils.getUser()));
    }


    @GetMapping("/comment")
    public ResponseEntity<List<MyCommentResponseDto>> myComment(){
        return ResponseEntity.ok(myAdoptService.myCommentList(SecurityUtils.getUser()));
    }
}

