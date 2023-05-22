package com.adoptpet.server.commons.image.service;

import com.adoptpet.server.commons.config.AwsS3config;
import com.adoptpet.server.commons.properties.AwsS3Properties;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.io.InputStream;
import java.net.URL;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

import static com.amazonaws.services.s3.model.DeleteObjectsRequest.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AwsS3Repository {

    private final AwsS3config awsS3config;
    private final AwsS3Properties awsS3Properties;


    //== S3 이미지 파일 업로드 ==//
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


    /**
    * S3 파일 단일 삭제
    **/
    public String deleteFile(String keyName) {
        final AmazonS3Client aswS3Client = awsS3config.amazonS3Client();
        final String bucket = awsS3Properties.getBucket();

        String result = "AWS S3 - Success";

        boolean isObjectExist = aswS3Client.doesObjectExist(bucket, keyName);

        if(isObjectExist){
            aswS3Client.deleteObject(bucket,keyName);
        } else {
            log.debug("Fail Delete Failed _ file not found");
            result = "AWS S3 - Failed _ file not found";
        }
        return result;
    }

    /**
     * S3 파일 다중 삭제
    **/
    public String deleteFiles(List<String> keyNames){

        // AmazonS3Client 인스턴스를 생성하여 S3 클라이언트에 연결한다.
        final AmazonS3Client aswS3Client = awsS3config.amazonS3Client();

        // 사용할 S3 버킷 이름을 가져온다.
        final String bucket = awsS3Properties.getBucket();

        // 삭제할 객체 키(Key) 및 버전(KeyVersion)을 저장할 리스트를 생성한다.
        List<KeyVersion> keys = new ArrayList<>();

        // 전달받은 keyNames 리스트의 각 요소를 KeyVersion 객체로 생성하여 keys 리스트에 추가한다.
        for(String keyName : keyNames){
            keys.add(new KeyVersion(keyName));
        }

        // 삭제 요청 객체(DeleteObjectsRequest)를 생성한다.
        // 해당 버킷(bucket)에서 keys 리스트에 있는 객체들을 삭제하도록 설정하고, quiet 모드를 비활성화(false)한다.
        DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(bucket)
                .withKeys(keys)
                .withQuiet(false);

        // deleteObjects 메서드를 호출하여 객체를 삭제하고, 삭제 결과(DeleteObjectsResult)를 받는다.
        DeleteObjectsResult deleteResult = aswS3Client.deleteObjects(deleteRequest);

        // 요청으로 전달된 키의 개수(requestKeys), 성공적으로 삭제된 객체 수(successfulDeletes), 남은 키 수(remainingKey)를 계산한다.
        final int requestKeys = keyNames.size();
        final int successfulDeletes = deleteResult.getDeletedObjects().size();
        final int remainingKey = requestKeys - successfulDeletes;

        String result = "";

        // 남은 키가 0이 아닌 경우, 삭제되지 않은 이미지 파일 개수를 메시지에 포함하여 결과를 설정한다.
        if(remainingKey != 0 ){
            result = String.format("AWS S3 - [%d]개의 이미지가 삭제되지 않았습니다.", remainingKey);
        }

        // 그렇지 않은 경우, 모든 이미지 파일이 성공적으로 삭제되었음을 나타내는 결과를 설정한다.
        result = "AWS S3 - Success delete all image file";

        // 최종 결과를 반환한다.
        return result;
    }
}
