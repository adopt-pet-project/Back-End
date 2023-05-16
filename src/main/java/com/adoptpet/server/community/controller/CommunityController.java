package com.adoptpet.server.community.controller;

import com.adoptpet.server.commons.image.service.AwsS3Service;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.community.dto.CommunityDto;
import com.adoptpet.server.community.dto.request.RegisterArticleRequest;
import com.adoptpet.server.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
@Slf4j
public class CommunityController {

    private final CommunityService communityService;
    private final AwsS3Service awsS3Service;

    @PostMapping
    public ResponseEntity<Void> writeArticle (@RequestPart(name = "data") @Valid RegisterArticleRequest request,
                                              @RequestPart(required = false) List<MultipartFile> images,
                                              BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        // 현재 회원의 인증 객체를 가져온다.
        SecurityUserDto user = SecurityUtils.getUser();

        // 이미지 업로드
//        List<ImageUploadResponse> imageUploadResponses = awsS3Service.uploadList(user.getEmail(), images, "community/article");

        // 게시글 dto 생성
        CommunityDto communityDto = CommunityDto.builder()
                .categoryNo(request.getCategoryNo())
                .title(request.getTitle())
                .content(request.getContent())
                .regId(user.getEmail())
                .modId(user.getEmail())
                .visibleYn(request.getVisibleYn())
                .build();



        return ResponseEntity.ok().build();
    }


}
