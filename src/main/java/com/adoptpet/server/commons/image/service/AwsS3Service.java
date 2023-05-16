package com.adoptpet.server.commons.image.service;

import com.adoptpet.server.adopt.repository.AdoptImageRepository;
import com.adoptpet.server.commons.image.ImageTypeEnum;
import com.adoptpet.server.community.domain.CommunityImage;
import com.adoptpet.server.community.repository.CommunityImageRepository;
import com.adoptpet.server.user.domain.ProfileImage;
import com.adoptpet.server.user.repository.ProfileImageRepository;
import com.amazonaws.services.s3.model.ObjectMetadata;
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

    private final AwsS3Repository awsS3Repository;
    private final CommunityImageRepository communityImageRepository;
    private final ProfileImageRepository profileImageRepository;
    private final AdoptImageRepository adoptImageRepository;

    private static String DOT = ".";


    @Transactional
    public String delete(ImageTypeEnum type, String fileName){
        String path = type.getPath();

        // 경로와 파일이름을 합쳐 S3 key name을 얻음
        String keyName = getKeyName(path, fileName);

        /*
           S3 이미지 삭제 요청
           성공시 result = success, 실패시 result = file not found
         */
        String result = awsS3Repository.deleteFile(keyName);

        return result;
    }


    /**
     * 전달받은 MultipartFile을 AwsS3 버킷에 업로드
     *
     * @param file        : 업로드할 MultipartFile
     * @param parentNo    : 이미지의 부모 pk
     * @param regId       : 작성자 email id
     * @param type        : 이미지 분류 코드
     * @return Integer    : 저장된 이미지의 id
     * @throws IllegalArgumentException : 타입이 유효하지 않은 값으로 입력되었을 경우 예외 발생
     * @throws ResponseStatusException  : 파일 업로드가 실패한 경우 예외가 발생
     */
    @Transactional
    public Integer upload(MultipartFile file, Integer parentNo, String regId, ImageTypeEnum type) {

        // 업로드할 파일에 대한 메타데이터 생성
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());          // 파일 크기
        objectMetadata.setContentType(file.getContentType());     // 파일 타입

        // 원본 파일명 가져오기
        final String imageName = file.getOriginalFilename();

        // 업로드될 파일을 위해 고유한 파일명 생성
        final String fileName = createFileName(imageName);

        try (InputStream inputStream = file.getInputStream()){

            // 경로와 파일이름을 합쳐 S3 key name을 얻음
            final String keyName = getKeyName(type.getPath(), fileName);
            log.info("keyName : {}" , keyName);

            URL responseUrl = awsS3Repository.uploadFile(objectMetadata, inputStream, fileName ,keyName);

            // Server DB에 upload된 image 정보 저장
            Integer savedImageNo = saveImageData(regId, parentNo, type, responseUrl, imageName);

            // 저장된 이미지 PK 반환
            return savedImageNo;

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패하셨습니다");
        }
    }


    //== 타입별 분류 후 데이터베이스 저장  ==//
    private Integer saveImageData(String regId, Integer parentNo, ImageTypeEnum type, URL responseUrl, String imageName){

        String imageUrl = combineUrls(type.getPath(), responseUrl);
        String extension = extractExtension(responseUrl.getFile());

        int savedImageNo;

        switch (type) {
            case COMMUNITY:
                savedImageNo = savedCommunityImage(regId, parentNo, imageName, imageUrl, extension);
                break;
            case PROFILE:
                savedImageNo = savedProfileImage(regId,parentNo,imageName,imageUrl,extension);
                break;
            case ADOPT:
                savedImageNo = 0;
                break;
            default:
                throw new IllegalArgumentException("Invalid type");
        }

        return savedImageNo;
    }

    //== 게시글이미지 정보 저장 ==//
    private Integer savedCommunityImage(String regId, Integer articleNo, String imageName, String imageUrl, String type) {
        CommunityImage community =
                CommunityImage.builder()
                .articleNo(articleNo)
                .build();

        community.addRagId(regId);
        community.addImageUrl(imageUrl);
        community.addImageName(imageName);
        community.addImageType(type);

        CommunityImage save = communityImageRepository.save(community);
        return save.getPictureNo();
    }
    
    //== 프로필이미지 정보 저장 ==//
    private Integer savedProfileImage(String regId, Integer memberNo, String imageName, String imageUrl, String type) {
        ProfileImage profile = ProfileImage.builder()
                .memberNo(memberNo)
                .build();

        profile.addRagId(regId);
        profile.addImageUrl(imageUrl);
        profile.addImageName(imageName);
        profile.addImageType(type);

        ProfileImage save = profileImageRepository.save(profile);
        return save.getPictureNo();
    }

    private void savedAdoptImage(String regId, Integer saleNo, String imageName, String imageUrl, String type) {

    }

    //== URL 결합 메서드 ==//
    private String combineUrls(String uploadPath, URL responseUrl) {
        StringBuilder sb = new StringBuilder();
        // prefix(프로토콜 + 호스트)
        sb.append(responseUrl.getProtocol());
        sb.append("://");
        sb.append(responseUrl.getHost());

        // URL path(경로 + 파일명)
        sb.append("/");
        sb.append(uploadPath);
        sb.append(responseUrl.getFile());

        return String.valueOf(sb);
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

    //== keyName 결합 메서드 ==//
    private String getKeyName(String path, String fileName) {

        StringBuilder sb = new StringBuilder();
        sb.append(path);
        sb.append("/");
        sb.append(fileName);

        return String.valueOf(sb);
    }

    //== 확장자 추출 메서드 ==//
    private String extractExtension(String fileName){
       return fileName.substring(fileName.indexOf(DOT) + 1);
    }






}
