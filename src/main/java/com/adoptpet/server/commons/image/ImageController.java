//package com.adoptpet.server.commons.image;
//
//import com.adoptpet.server.commons.image.dto.request.ImageDeleteRequest;
//import com.adoptpet.server.commons.image.dto.request.ImageUploadRequest;
//import com.adoptpet.server.commons.image.service.AwsS3Service;
//import com.adoptpet.server.commons.security.dto.SecurityUserDto;
//import com.adoptpet.server.commons.util.SecurityUtils;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//
//
//@RestController
//@RequestMapping("/image")
//@RequiredArgsConstructor
//@Slf4j
//public class ImageController {
//
//    private final AwsS3Service awsS3Service;
//
//    @PostMapping(value = "",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<Integer> uploadImage(
//            @RequestPart(name = "image") MultipartFile images,
//            @RequestPart(name = "data") ImageUploadRequest request){
//
//        // 현재 회원의 인증 객체를 가져온다.
//        SecurityUserDto user = SecurityUtils.getUser();
//
//        Integer savedImageNo = awsS3Service.upload(images, request.getContentNo(), user.getEmail(), request.getType());
//
//        return ResponseEntity.ok(savedImageNo);
//    }
//
//    /**
//     *  임시 delete
//     *  - 정책에 맞춰 변경될 예정
//     **/
//    @DeleteMapping
//    public ResponseEntity<String> deleteImage(@RequestBody ImageDeleteRequest request){
//
//        String result = awsS3Service.delete(request.getType(), request.getFileName());
//
//        return ResponseEntity.ok(result);
//    }
//
//}
