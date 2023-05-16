package com.adoptpet.server.commons.image.service;

import com.adoptpet.server.commons.config.AwsS3config;
import com.adoptpet.server.commons.properties.AwsS3Properties;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

@Repository
@RequiredArgsConstructor
public class AwsS3Repository {

    private final AwsS3config awsS3config;
    private final AwsS3Properties awsS3Properties;


    //== S3에 이미지 파일을 업로드 ==//
    public URL uploadFile(ObjectMetadata objectMetadata, InputStream inputStream, String fileName,  String keyName) {

        final AmazonS3Client aswS3Client = awsS3config.amazonS3Client();
        final String bucket = awsS3Properties.getBucket();
        // 파일을 AwsS3 버킷에 업로드(취소되었을 경우 실행 로직 작성 예정)
        aswS3Client.putObject(
                new PutObjectRequest(bucket, keyName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        // 업로드된 파일의 URL 가져온다.
        URL responseUrl = aswS3Client.getUrl(bucket, fileName);

        return responseUrl;
    }


    public String deleteFile(String keyName) {
        final AmazonS3Client aswS3Client = awsS3config.amazonS3Client();
        final String bucket = awsS3Properties.getBucket();

        String result = "success";

        boolean isObjectExist = aswS3Client.doesObjectExist(bucket, keyName);

        if(isObjectExist){
            aswS3Client.deleteObject(bucket,keyName);
        } else {
            result = "file not found";
        }

        return result;
    }

}
