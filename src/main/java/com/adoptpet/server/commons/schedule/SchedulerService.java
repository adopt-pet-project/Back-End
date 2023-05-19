//package com.adoptpet.server.commons.schedule;
//
//import com.adoptpet.server.adopt.domain.AdoptImage;
//import com.adoptpet.server.adopt.repository.AdoptImageRepository;
//import com.adoptpet.server.commons.image.ImageTypeEnum;
//import com.adoptpet.server.commons.image.service.AwsS3Repository;
//import com.adoptpet.server.commons.image.service.AwsS3Service;
//import com.adoptpet.server.community.domain.CommunityImage;
//import com.adoptpet.server.community.repository.CommunityImageRepository;
//import com.adoptpet.server.user.domain.FeedbackImage;
//import com.adoptpet.server.user.domain.ProfileImage;
//import com.adoptpet.server.user.domain.ReportImage;
//import com.adoptpet.server.user.repository.FeedbackImageRepository;
//import com.adoptpet.server.user.repository.ProfileImageRepository;
//import com.adoptpet.server.user.repository.ReportImageRepository;
//import com.amazonaws.services.ec2.model.Image;
//import com.amazonaws.util.CollectionUtils;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class SchedulerService {
//
//    private final AwsS3Service awsS3Service;
//    private final ProfileImageRepository profileImageRepository;
//    private final CommunityImageRepository communityImageRepository;
//    private final AdoptImageRepository adoptImageRepository;
//    private final ReportImageRepository reportImageRepository;
//    private final FeedbackImageRepository feedbackImageRepository;
//
//    @Scheduled(cron = "* 5 * * * * ", zone = "Asia/Seoul")
//    public void deleteImage() {
//        log.info("======== 이미지 삭제 작업 스케줄러 동작 ==========");
//
//        List<ProfileImage> profileImages = profileImageRepository.findAllProfileImageNull();
//        List<CommunityImage> communityImages = communityImageRepository.findAllCommunityImageNull();
//        List<AdoptImage> adoptImages = adoptImageRepository.findAllAdoptImageNull();
//        List<ReportImage> reportImages = reportImageRepository.findAllReportImageNull();
//        List<FeedbackImage> feedbackImages = feedbackImageRepository.findAllFeedbackImageNull();
//
//        if (!CollectionUtils.isNullOrEmpty(profileImages)) {
//            log.info("========= 프로필 이미지 삭제 시작 ==========");
//            profileImages.stream()
//                    .forEach(profileImage -> awsS3Service.deleteFileByUrl(ImageTypeEnum.valueOf(profileImage.getImageType()), profileImage.getImageUrl()));
//            profileImageRepository.deleteAll(profileImages);
//            log.info("========= 프로필 이미지 삭제 완료 ==========");
//        }
//
//        if (!CollectionUtils.isNullOrEmpty(adoptImages)) {
//            log.info("========= 분양 이미지 삭제 시작 ==========");
//            adoptImages.stream()
//                    .forEach(adoptImage -> awsS3Service.deleteFileByUrl(ImageTypeEnum.valueOf(adoptImage.getImageType()), adoptImage.getImageUrl()));
//            adoptImageRepository.deleteAll(adoptImages);
//            log.info("========= 분양 이미지 삭제 완료 ==========");
//        }
//
//        if (!CollectionUtils.isNullOrEmpty(communityImages)) {
//            log.info("========= 게시글 이미지 삭제 시작 ==========");
//            communityImages.stream()
//                    .forEach(communityImage -> awsS3Service.deleteFileByUrl(ImageTypeEnum.valueOf(communityImage.getImageType()), communityImage.getImageUrl()));
//            communityImageRepository.deleteAll(communityImages);
//            log.info("========= 게시글 이미지 삭제 완료 ==========");
//        }
//
//        if (!CollectionUtils.isNullOrEmpty(reportImages)) {
//            log.info("========= 신고 이미지 삭제 시작 ==========");
//            //        reportImages.stream()
////                .forEach(reportImage -> awsS3Service.deleteFileByUrl(ImageTypeEnum.valueOf(reportImage.getImageType()), reportImage.getImageUrl()));
//            reportImageRepository.deleteAll(reportImages);
//            log.info("========= 신고 이미지 삭제 완료 ==========");
//        }
//
//        if (!CollectionUtils.isNullOrEmpty(feedbackImages)) {
//            log.info("========= 피드백 이미지 삭제 시작 ==========");
//            //        feedbackImages.stream()
////                .forEach(feedbackImage -> awsS3Service.deleteFileByUrl(ImageTypeEnum.valueOf(feedbackImage.getImageType()), feedbackImage.getImageUrl()));
//            feedbackImageRepository.deleteAll(feedbackImages);
//            log.info("========= 피드백 이미지 삭제 완료 ==========");
//        }
//
//    }
//}
