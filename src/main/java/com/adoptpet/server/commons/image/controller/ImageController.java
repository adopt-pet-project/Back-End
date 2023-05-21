package com.adoptpet.server.commons.image.controller;

import com.adoptpet.server.commons.exception.ErrorCode;
import com.adoptpet.server.commons.image.service.AwsS3Service;
import com.adoptpet.server.commons.support.StatusResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
     * @param file    : 이미지 파일
     * @param type    : 이미지 분류(community,adopt,profile ...)
     * @return I
     **/
    @PostMapping(value = "",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<StatusResponseDto> uploadImage(
            @RequestPart(name = "file") MultipartFile file,
            @RequestPart(name = "type") String type,
            @RequestPart(name = "email",required = false) String email,
            @RequestHeader(value = "Authorization",required = false) String accessToken){

        // request validation
        if(!StringUtils.hasText(email) && !StringUtils.hasText(accessToken)){
            ErrorCode.throwNotEnterEmailOrToken();
        } else if(file.isEmpty()){
            ErrorCode.throwMissingFileRequestPart();
        }

        // image 업로드(S3, DB)
        awsS3Service.upload(file, type, email, accessToken);

        return ResponseEntity.ok(StatusResponseDto.success());
    }

    /**
     *  단일 image delete API
     * @param type    : 이미지 분류(community,adopt,profile ...)
     * @param imageNo : 이미지 sequence
     * @return String : 이미지 파일 업로드 결과
     *         - 성공시 : "AWS S3 - Success"
     *         - 실패시 : "AWS S3 - Failed _ file not found"
     **/
    @DeleteMapping("/{type}/{imageNo}")
    public ResponseEntity<StatusResponseDto> deleteImage(
            @PathVariable(name = "type") String type,
            @PathVariable(name = "imageNo") Integer imageNo
    ){

        // image 삭제(S3, DB)
        String result = awsS3Service.delete(type, imageNo);

        return ResponseEntity.ok(StatusResponseDto.success(result));
    }
}