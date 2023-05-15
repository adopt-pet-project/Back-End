package com.adoptpet.server.commons.image.service;

import com.adoptpet.server.commons.config.AwsS3config;
import com.adoptpet.server.commons.image.dto.ImageDto;
import com.adoptpet.server.commons.properties.AwsS3Properties;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AwsS3Service {

    private final AwsS3config awsS3config;
    private final AwsS3Properties awsS3Properties;

    private static String DOT = ".";


    /**
     * 전달받은 MultipartFile을 AwsS3 버킷에 업로드
     *
     * @param file        : 업로드할 MultipartFile
     * @param uploadPath  : AwsS3 버킷에서의 대상 경로
     * @return S3ImageDto : 업로드된 파일의 URL과 원본 파일명을 담은 DTO
     * @throws IllegalArgumentException : 파일명이 잘못된 경우 예외가 발생
     * @throws ResponseStatusException  : 파일 업로드가 실패한 경우 예외가 발생
     */
    public ImageDto upload(MultipartFile file, String uploadPath) {

        // 업로드할 파일에 대한 메타데이터 생성
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());          // 파일 크기
        objectMetadata.setContentType(file.getContentType());     // 파일 타입

        // 원본 파일명 가져오기
        String originalFileName = file.getOriginalFilename();

        // 업로드된 파일을 위해 고유한 파일명 생성
        final String fileName = createFileName(originalFileName);

        try (InputStream inputStream = file.getInputStream()){
            final String keyName = uploadPath + "/" + fileName;

            AmazonS3Client amazonS3Client = awsS3config.amazonS3Client();
            String bucket = awsS3Properties.getBucket();

            // 파일을 AwsS3 버킷에 업로드(취소되었을 경우 실행 로직 작성 예정)
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket,keyName,inputStream,objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            // 업로드된 파일의 URL 가져와서 imageKey 추출하기
            URL imageUrl = amazonS3Client.getUrl(bucket, fileName);
            String imageKey = imageUrl.getFile().replace("/", "");

            // URL과 원본 파일명을 가진 S3ImageDto 생성하여 반환
            return new ImageDto(uploadPath,imageKey,originalFileName,extractExtension(fileName));

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패하셨습니다");
        }
    }


    //== 이미지 파일명 난수화 메서드 ==//
    private String createFileName(String fileName){

        String extension = extractExtension(fileName);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(UUID.randomUUID());
        stringBuilder.append(DOT);
        stringBuilder.append(extension);

        return stringBuilder.toString();
    }

    private String extractExtension(String fileName){
       return fileName.substring(fileName.indexOf(DOT) + 1);
    }






}
