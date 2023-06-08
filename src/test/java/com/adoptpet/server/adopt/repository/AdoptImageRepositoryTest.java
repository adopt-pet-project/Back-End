//package com.adoptpet.server.adopt.repository;
//
//import com.adoptpet.server.adopt.domain.AdoptImage;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//
//@DataJpaTest
//class AdoptImageRepositoryTest {
//
//    @Autowired
//    AdoptImageRepository adoptImageRepository;
//
//    @Test
//    @DisplayName("분양 게시글 이미지 테이블의 테이블 키값을 업데이트 테스트")
//    /*
//    *   테스트 시나리오
//    *   1. given -> AdoptImage 3개를 저장
//    *   2. when -> AdoptImage의 saleNo를 3으로 전부 업데이트 뒤 List로 조회
//    *   3. then -> 업데이트 된 AdoptImage의 값들을 검증(정렬 순서(sort), 테이블 키 값(saleNo)
//    * */
//    void updateImageSaleNo() {
//        // given
//        AdoptImage adoptImage1 = new AdoptImage("/img/picture1", "사모예드1", "jpeg", null);
//        AdoptImage adoptImage2 = new AdoptImage("/img/picture2", "사모예드2", "png", null);
//        AdoptImage adoptImage3 = new AdoptImage("/img/picture3", "사모예드3", "gif", null);
//
//        adoptImage1 = adoptImageRepository.save(adoptImage1);
//        adoptImage2 = adoptImageRepository.save(adoptImage2);
//        adoptImage3 = adoptImageRepository.save(adoptImage3);
//
//        // when
//        adoptImageRepository.updateAdoptImageSaleNo(3, adoptImage1.getPictureNo(), 2);
//        adoptImageRepository.updateAdoptImageSaleNo(3, adoptImage2.getPictureNo(), 1);
//        adoptImageRepository.updateAdoptImageSaleNo(3, adoptImage3.getPictureNo(), 3);
//
//        List<AdoptImage> images = adoptImageRepository.findAll();
//
//        // then
//        assertThat(images.get(0).getSort()).isEqualTo(2);
//        assertThat(images.get(1).getSort()).isEqualTo(1);
//        assertThat(images.get(2).getSort()).isEqualTo(3);
//
//        assertThat(images.get(0).getSaleNo()).isEqualTo(3).isEqualTo(images.get(1).getSaleNo())
//                .isEqualTo(images.get(2).getSaleNo());
//    }
//
//    @Test
//    @DisplayName("분양 게시글 이미지 테이블의 테이블 키값을 null로 업데이트 Test")
//    /*
//    *   테스트 시나리오
//    *   1. given -> AdoptImage 3개를 저장 -> saleNo를 3으로
//    *   2. when -> 저장한 AdoptImage의 saleNo를 null로 전부 변경 후 List 조회증
//    *   3. then -> AdoptImage의 saleNo 값이 전부 null인지 검증
//    * */
//    void updateImageSaleNull() {
//        // given
//        AdoptImage adoptImage1 = new AdoptImage("/img/picture1", "사모예드1", "jpeg", 3);
//        AdoptImage adoptImage2 = new AdoptImage("/img/picture2", "사모예드2", "png", 3);
//        AdoptImage adoptImage3 = new AdoptImage("/img/picture3", "사모예드3", "gif", 3);
//
//        adoptImageRepository.save(adoptImage1);
//        adoptImageRepository.save(adoptImage2);
//        adoptImageRepository.save(adoptImage3);
//
//        // when
//        adoptImageRepository.updateAdoptImageNull(3);
//
//        List<AdoptImage> images = adoptImageRepository.findAll();
//
//        // then
//        assertThat(images.get(0).getSaleNo()).isNull();
//        assertThat(images.get(1).getSaleNo()).isNull();
//        assertThat(images.get(2).getSaleNo()).isNull();
//    }
//}