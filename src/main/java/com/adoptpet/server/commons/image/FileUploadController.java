package com.adoptpet.server.commons.image;

//import com.adoptpet.server.commons.properties.AwsS3Properties;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/user/stest")
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {

    @GetMapping
    public String test(){
        return "test";
    }

    @PostMapping
    public String postTest(@RequestBody String test){
        return "test success";

    }

//    private final AmazonS3Client amazonS3Client;
//    private final AwsS3Properties awsS3Properties;

//    @PutMapping
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//
//        log.info("content :{}",file.getContentType());

//        String bucket = awsS3Properties.getBucket();
//
//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentType(file.getContentType());
//        objectMetadata.setContentLength(file.getSize());
//
//        String key = "item/" + file.getOriginalFilename();
//
//        try (InputStream inputStream = file.getInputStream()) {
//            final PutObjectRequest putObjectRequest = new PutObjectRequest(
//                    bucket,
//                    key,
//                    inputStream,
//                    objectMetadata);
//            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
//
//           amazonS3Client.putObject(putObjectRequest);
//
//           return ResponseEntity.ok(file.getName());
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(e.hashCode()).body(e.getMessage());
//        }

//    return ResponseEntity.ok("test");
//    }


}
