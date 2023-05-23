package com.adoptpet.server.commons.image.service;

import com.adoptpet.server.adopt.domain.AdoptImage;
import com.adoptpet.server.adopt.repository.AdoptImageRepository;
import com.adoptpet.server.commons.exception.CustomException;
import com.adoptpet.server.commons.exception.ErrorCode;
import com.adoptpet.server.commons.image.ImageTypeEnum;
import com.adoptpet.server.commons.image.dto.ImageInfoDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.community.domain.CommunityImage;
import com.adoptpet.server.community.repository.CommunityImageRepository;
import com.adoptpet.server.user.domain.ProfileImage;
import com.adoptpet.server.user.repository.ProfileImageRepository;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.adoptpet.server.commons.exception.ErrorCode.*;

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
    public String delete(String type, Integer imageNo){
        // 타입 분류를 위해 enum 생성
        ImageTypeEnum typeEnum = ImageTypeEnum.from(type.toLowerCase());

        String imageUrl ="";

        switch (typeEnum){
            case ADOPT:
                // image 조회
                AdoptImage adoptImg = adoptImageRepository.findById(imageNo)
                        .orElseThrow(ErrorCode::throwImageNotFound);
                imageUrl = adoptImg.getImageUrl();

                // DB 이미지 정보 제거
                adoptImageRepository.delete(adoptImg);
                break;
            case COMMUNITY:
                // image 조회
                CommunityImage communityImg = communityImageRepository.findById(imageNo)
                        .orElseThrow(ErrorCode::throwImageNotFound);
                imageUrl = communityImg.getImageUrl();

                // DB 이미지 정보 제거
                communityImageRepository.delete(communityImg);
                break;
            case PROFILE:
                // image 조회
                ProfileImage profileImg = profileImageRepository.findById(imageNo)
                        .orElseThrow(ErrorCode::throwImageNotFound);
                imageUrl = profileImg.getImageUrl();

                // DB 이미지 정보 제거
                profileImageRepository.delete(profileImg);
                break;
        }

        String result = deleteFile(imageUrl);

        // S3 이미지 파일 삭제 요청
        return result;

    }

    /**
     * S3 단일 파일 삭제
     * @param imageUrl : S3 image URL
     * @return String : 성공시 result = success, 실패시 result = file not found
     **/
    @Transactional
    public String deleteFile(String imageUrl) {

        // URL 에서 keyName 추출
        final String keyName = extractKeyName(imageUrl);
        log.info("keyName : {} ", keyName);

        // keyName으로 S3에 delete request
        String result = awsS3Repository.deleteFile(keyName);

        return result;
    }

    /**
    * S3 여러 파일 삭제
     * @param imageUrls : S3 image URL 리스트
     * @return String  :
     *          - 성공시 : result = "Success delete all image file"
     *          - 실패시 : result = "n개의 이미지가 삭제되지 않았습니다."
    **/
    @Transactional
    public String deleteMultipleFile(List<String> imageUrls){
        // 이미지 URL에서 keyName 추출
        List<String> keyNames = imageUrls.stream()
                .map(url -> extractKeyName(url))
                .collect(Collectors.toList());

        // S3에 삭제 요청
        String result = awsS3Repository.deleteFiles(keyNames);

        return result;
    }

    //== 이미지 URL 에서 keyName 추출 ==//
    public String extractKeyName(String imageUrl){

        final int secondSlashIndex = imageUrl.indexOf("/") + 2;
        final int thirdSlashIndex = imageUrl.indexOf("/", secondSlashIndex);

        final String keyName = imageUrl.substring(thirdSlashIndex + 1);

        return keyName;
    }

    /**
     * 전달받은 MultipartFile을 AwsS3 버킷에 업로드
     *
     * @param file        : 업로드할 MultipartFile
     * @param regId       : 작성자 email id
     * @param type        : 이미지 분류 코드
     * @return Integer    : 저장된 이미지의 id
     * @throws IllegalArgumentException : 타입이 유효하지 않은 값으로 입력되었을 경우 예외 발생
     * @throws ResponseStatusException  : 파일 업로드가 실패한 경우 예외가 발생
     */
    @Transactional
    public ImageInfoDto upload(MultipartFile file, String type, String regId, String accessToken) {

        // 타입 분류를 위해 enum 생성
        ImageTypeEnum typeEnum = ImageTypeEnum.from(type.toLowerCase());

        // 업로드할 파일에 대한 메타데이터 생성
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());          // 파일 크기
        objectMetadata.setContentType(file.getContentType());     // 파일 타입

        // 토큰이 있을 경우 해당 토큰에서 이메일을 얻어 등록자 ID 초기화
        if(StringUtils.hasText(accessToken)){
            regId = SecurityUtils.getUser().getEmail();
        }

        // 원본 파일명 가져오기
        final String imageName = file.getOriginalFilename();
        // 업로드될 파일을 위해 고유한 파일명 생성
        final String fileName = createFileName(imageName);

        try (InputStream inputStream = file.getInputStream()){

            // 경로와 파일이름을 합쳐 S3 key name을 얻음
            final String keyName = extractKeyName(typeEnum.getPath(), fileName);

            // 파일 업로드 후 파일에 대한 URL을 얻음
            URL responseUrl = awsS3Repository.uploadFile(objectMetadata, inputStream, fileName ,keyName);

            // Server DB에 upload된 image 정보 저장
            ImageInfoDto savedImageInfo = saveImage(regId, typeEnum, responseUrl, imageName);

            // 저장된 이미지 PK 반환
            return savedImageInfo;
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new CustomException(UNSUCCESSFUL_UPLOAD);
        }
    }

    //== 타입별 분류 후 데이터베이스 저장  ==//
    private ImageInfoDto saveImage(String regId, ImageTypeEnum type, URL responseUrl, String imageName){

        String imageUrl = combineUrls(type.getPath(), responseUrl);
        String extension = extractExtension(responseUrl.getFile());

        SaveImageData saveImageData = SaveImageData.builder()
                .regId(regId)
                .imageUrl(imageUrl)
                .imageName(imageName)
                .extension(extension)
                .build();

        ImageInfoDto imageInfo;

        switch (type) {
            case COMMUNITY:
                imageInfo = saveCommunityImage(saveImageData);
                break;
            case PROFILE:
                imageInfo = saveProfileImage(saveImageData);
                break;
            case ADOPT:
                imageInfo = saveAdoptImage(saveImageData);
                break;
            default:
                throw new CustomException(TYPE_NOT_FOUND);
        }

        return imageInfo;
    }


    //== 게시글이미지 정보 저장 ==//
    private ImageInfoDto saveCommunityImage(SaveImageData imageData) {
        CommunityImage community = CommunityImage.builder()
                .build();

        community.addRagId(imageData.getRegId());
        community.addImageUrl(imageData.getImageUrl());
        community.addImageName(imageData.getImageName());
        community.addImageType(imageData.getExtension());

        CommunityImage save = communityImageRepository.save(community);
        return new ImageInfoDto(save.getPictureNo(),save.getImageUrl());
    }

    //== 프로필이미지 정보 저장 ==//
    private ImageInfoDto saveProfileImage(SaveImageData imageData) {
        ProfileImage profile = ProfileImage.builder()
                .build();

        profile.addRagId(imageData.getRegId());
        profile.addImageUrl(imageData.getImageUrl());
        profile.addImageName(imageData.getImageName());
        profile.addImageType(imageData.getExtension());

        ProfileImage save = profileImageRepository.save(profile);

        return new ImageInfoDto(save.getPictureNo(),save.getImageUrl());
    }

    private ImageInfoDto saveAdoptImage(SaveImageData imageData) {
        AdoptImage image = AdoptImage.builder()
                .build();

        image.addRagId(imageData.getRegId());
        image.addImageUrl(imageData.getImageUrl());
        image.addImageName(imageData.getImageName());
        image.addImageType(imageData.getExtension());

        AdoptImage save = adoptImageRepository.save(image);

        return new ImageInfoDto(save.getPictureNo(),save.getImageUrl());
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
    private String extractKeyName(String path, String fileName) {

        StringBuilder sb = new StringBuilder();
        sb.append(path);
        sb.append("/");
        sb.append(fileName);

        return String.valueOf(sb);
    }

    //== 확장자 추출 메서드 ==//
    private String extractExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf(DOT) + 1);
    }


    @Getter
    @Builder
    @AllArgsConstructor
    private static class SaveImageData {

        String regId;
        String imageName;
        String imageUrl;
        String extension;
    }
}
