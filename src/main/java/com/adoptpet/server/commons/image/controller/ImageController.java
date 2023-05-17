package com.adoptpet.server.commons.image.controller;

import com.adoptpet.server.commons.image.service.AwsS3Service;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final AwsS3Service awsS3Service;

    /**
    * 단일 image upload API
     * @param image   : 이미지 파일
     * @param type    : 이미지 분류(community,adopt,profile ...)
     * @return I
    **/
    @PostMapping(value = "",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Integer> uploadImage(
            @RequestPart(name = "imageFile") MultipartFile image,
            @RequestPart(name = "type") String type){

        // 현재 회원의 인증 객체를 가져온다.
        SecurityUserDto user = SecurityUtils.getUser();

        Integer savedImageNo = awsS3Service.upload(image, user.getEmail(), type);

        return ResponseEntity.ok(savedImageNo);
    }

    /**
     *  단일 image delete API
     * @param type    : 이미지 분류(community,adopt,profile ...)
     * @param imageNo : 이미지 sequence
     * @return String : 이미지 파일 업로드 결과
     *         - 성공시 : "AWS S3 - Success"
     *         - 실패시 : "AWS S3 - Failed _ file not found"
     **/
    @DeleteMapping("/{imageType}/{imageNo}")
    public ResponseEntity<String> deleteImage(
            @PathVariable(name = "imageType") String type,
            @PathVariable(name = "imageNo") Integer imageNo){

        String result = awsS3Service.delete(type, imageNo);

        return ResponseEntity.ok(result);
    }

}
