package com.adoptpet.server.community.service;

import com.adoptpet.server.community.domain.BlindYnEnum;
import com.adoptpet.server.community.domain.LogicalDelEnum;
import com.adoptpet.server.community.domain.VisibleYnEnum;
import com.adoptpet.server.community.dto.CommunityDto;
import com.adoptpet.server.community.repository.CommunityQDslRepository;
import com.adoptpet.server.community.repository.CommunityRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CommunityServiceTest {


    @Autowired
    private CommunityService communityService;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CommunityQDslRepository communityQDslRepository;


//    @Test
//    @DisplayName("게시글 등록 테스트")
//    void registerArticle_Success(){
//        //given
//        String memberEmail = "test10@gmail.com";
//        Integer categoryNo = 1;
//        Integer[] imgNo = {1,2,3};
//        CommunityDto communityDto = new CommunityDto(1,"테스트2", "코온텐츠", 0,
//                memberEmail, memberEmail, VisibleYnEnum.VISIBLE,
//                LogicalDelEnum.NORMAL, BlindYnEnum.NORMAL,imgNo);
//
//        //when
//        CommunityDto createdArticle = communityService.insertArticle(communityDto);
//
//        //then
//        Assertions.assertEquals("테스트2",createdArticle.getTitle(),"title 불일치");
//        Assertions.assertEquals("코온텐츠",createdArticle.getContent(),"content 불일치");
//        Assertions.assertEquals(VisibleYnEnum.VISIBLE,createdArticle.getVisibleYn(),"visible 불일치");
//    }

}