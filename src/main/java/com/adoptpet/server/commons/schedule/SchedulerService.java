package com.adoptpet.server.commons.schedule;

import com.adoptpet.server.adopt.domain.AdoptImage;
import com.adoptpet.server.adopt.repository.AdoptImageRepository;
import com.adoptpet.server.commons.image.ImageTypeEnum;
import com.adoptpet.server.commons.image.service.AwsS3Repository;
import com.adoptpet.server.commons.image.service.AwsS3Service;
import com.adoptpet.server.commons.support.BaseImageEntity;
import com.adoptpet.server.community.domain.CommunityImage;
import com.adoptpet.server.community.repository.CommunityImageRepository;
import com.adoptpet.server.user.domain.FeedbackImage;
import com.adoptpet.server.user.domain.ProfileImage;
import com.adoptpet.server.user.domain.ReportImage;
import com.adoptpet.server.user.repository.FeedbackImageRepository;
import com.adoptpet.server.user.repository.ProfileImageRepository;
import com.adoptpet.server.user.repository.ReportImageRepository;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.util.CollectionUtils;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final AwsS3Service awsS3Service;
    private final ProfileImageRepository profileImageRepository;
    private final CommunityImageRepository communityImageRepository;
    private final AdoptImageRepository adoptImageRepository;
    private final ReportImageRepository reportImageRepository;
    private final FeedbackImageRepository feedbackImageRepository;

    @Scheduled(cron = "0 12 * * * *", zone = "Asia/Seoul")
    public void deleteImage() {
        log.info("======== 이미지 삭제 작업 스케줄러 동작 ==========");

        List<ProfileImage> profileImages = profileImageRepository.findAllProfileImageNull();
        List<CommunityImage> communityImages = communityImageRepository.findAllCommunityImageNull();
        List<AdoptImage> adoptImages = adoptImageRepository.findAllAdoptImageNull();
//        List<ReportImage> reportImages = reportImageRepository.findAllReportImageNull();
//        List<FeedbackImage> feedbackImages = feedbackImageRepository.findAllFeedbackImageNull();

        deleteImages(profileImages, "profile");
        deleteImages(communityImages, "community");
        deleteImages(adoptImages, "adopt");
    }


    // Image Entity List로부터 URL을 추출하는 메서드
    private List<String> getImageUrls(List<? extends BaseImageEntity> imageList) {
        return imageList.stream()
                .map(BaseImageEntity::getImageUrl)
                .collect(Collectors.toList());
    }

    // 한번에 1000개까지 요청을 보낼 수 있도록 리스트를 분할하고, S3에 요청을 보내는 메서드
    private void deleteS3Images(List<String> imageList) {
        List<List<String>> deleteList = Lists.partition(imageList, 1000);
        for (List<String> urlList : deleteList) {
            awsS3Service.deleteMultipleFile(urlList);
        }
    }

    // 이미지를 삭제하는 메서드
    private void deleteImages(List<? extends BaseImageEntity> imageList, String type) {
        if (CollectionUtils.isNullOrEmpty(imageList)) {
            return;
        }

        switch (type) {
            case "profile" :
                profileImageRepository.deleteAll((List<ProfileImage>)imageList);
                deleteS3Images(getImageUrls(imageList));
                break;
            case "adopt" :
                adoptImageRepository.deleteAll((List<AdoptImage>)imageList);
                deleteS3Images(getImageUrls(imageList));
                break;
            case "community" :
                communityImageRepository.deleteAll((List<CommunityImage>)imageList);
                deleteS3Images(getImageUrls(imageList));
                break;
            default:
                throw new IllegalStateException("삭제하려는 이미지의 분류가 없습니다.");
        }
    }
}
