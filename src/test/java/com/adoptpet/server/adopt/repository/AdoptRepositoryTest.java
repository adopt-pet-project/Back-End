package com.adoptpet.server.adopt.repository;

import com.adoptpet.server.adopt.domain.Adopt;
import com.adoptpet.server.adopt.domain.Gender;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
class AdoptRepositoryTest {
    @Autowired
    AdoptRepository adoptRepository;

    @Test
    @DisplayName("분양글 기본키로 삭제 테스트")
    /*
    *   테스트 시나리오
    *   1. given -> 분양글을 한개 저장
    *   2. when -> 기본키값으로 분양글을 삭제
    *   3. then -> 삭제된 분양글의 키값으로 분양글을 조회 -> 값을 꺼낼때 예외 발생 검증
    * */
    void deleteAdoptBySaleNo() {
        // given
        Adopt adopt = Adopt.builder()
                .title("사모예드 분양해요")
                .content("이 귀여운 사모예드 너무 이뻐서 미치겠다.. 너무 귀여워")
                .age("1년 반")
                .gender(Gender.MAN)
                .kind("사모예드")
                .latitude(45.677f)
                .longitude(48.345f)
                .build();

        // when
        adoptRepository.save(adopt);
        adoptRepository.deleteBySaleNo(adopt.getSaleNo());

        // then
        assertThatThrownBy(() -> {
          adoptRepository.findById(adopt.getSaleNo()).orElseThrow(IllegalStateException::new);
        }).isInstanceOf(IllegalStateException.class);

    }
}