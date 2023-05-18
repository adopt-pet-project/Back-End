//package com.adoptpet.server.commons.image.service;
//
//import com.adoptpet.server.commons.config.AwsS3config;
//import com.adoptpet.server.commons.properties.AwsS3Properties;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Repository;
//
//import java.io.InputStream;
//import java.net.URL;
//
//@Slf4j
//@Repository
//@RequiredArgsConstructor
//public class AwsS3Repository {
//
//    private final AwsS3config awsS3config;
//    private final AwsS3Properties awsS3Properties;
//
//
//    //== S3 이미지 파일 업로드 ==//
//    public URL uploadFile(ObjectMetadata objectMetadata, InputStream inputStream, String fileName,  String keyName) {
//
//        final AmazonS3Client aswS3Client = awsS3config.amazonS3Client();
//        final String bucket = awsS3Properties.getBucket();
//        // 파일을 AwsS3 버킷에 업로드(취소되었을 경우 실행 로직 작성 예정)
//        aswS3Client.putObject(
//                new PutObjectRequest(bucket, keyName, inputStream, objectMetadata)
//                        .withCannedAcl(CannedAccessControlList.PublicRead)
//        );
//
//        // 업로드된 파일의 URL 가져온다.
//        URL responseUrl = aswS3Client.getUrl(bucket, fileName);
//
//        return responseUrl;
//    }
//
//    //== S3 이미지 파일 제거 ==//
//    public String deleteFile(String keyName) {
//        final AmazonS3Client aswS3Client = awsS3config.amazonS3Client();
//        final String bucket = awsS3Properties.getBucket();
//
//        String result = "AWS S3 - Success";
//
//        boolean isObjectExist = aswS3Client.doesObjectExist(bucket, keyName);
//
//        if(isObjectExist){
//            aswS3Client.deleteObject(bucket,keyName);
//        } else {
//            log.debug("Fail Delete Failed _ file not found");
//            result = "AWS S3 - Failed _ file not found";
//        }
//
//        return result;
//    }
//
//    /**
//     * 다중 삭제
//    * 작성중
//    **/
//    public void deleteFiles(String... keys){
//
//        final AmazonS3Client aswS3Client = awsS3config.amazonS3Client();
//        final String bucket = awsS3Properties.getBucket();
//
//        DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(bucket)
//                .withKeys(keys)
//                .withQuiet(false);
//
//        DeleteObjectsResult deleteResult = aswS3Client.deleteObjects(deleteRequest);
//
//        final int requestKeys = keys.length;
//        final int successfulDeletes = deleteResult.getDeletedObjects().size();
//        final int remainingKey = requestKeys - successfulDeletes;
//
//        String result;
//        if( remainingKey != 0 ){
//            result = String.format("AWS S3 - [%d] 개의 이미지가 삭제되지 않았습니다.", remainingKey);
//        }
//        result = "AWS S3 - Success delete all image file";
//    }
//
//}
