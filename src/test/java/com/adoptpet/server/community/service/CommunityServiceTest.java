package com.adoptpet.server.community.service;

import com.adoptpet.server.community.domain.BlindYnEnum;
import com.adoptpet.server.community.domain.LogicalDelEnum;
import com.adoptpet.server.community.domain.VisibleYnEnum;
import com.adoptpet.server.community.dto.CommunityDto;
import com.adoptpet.server.community.repository.CommunityRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CommunityServiceTest {


    @Autowired
    private CommunityService communityService;

    @Autowired
    private CommunityRepository communityRepository;

    @Test
    @Rollback(value = false)
    void createArticle_Success(){
        //given
        String memberEmail = "test10@gmail.com";
        Integer categoryNo = 1;
        CommunityDto communityDto = new CommunityDto("테스트2", "코온텐츠", 0,
                memberEmail, memberEmail, VisibleYnEnum.VISIBLE,
                LogicalDelEnum.NORMAL, BlindYnEnum.NORMAL);

        //when
        CommunityDto createdArticle = communityService.createArticle(communityDto, memberEmail, categoryNo);

        //then
        Assertions.assertEquals("테스트2",createdArticle.getTitle(),"title 불일치");
        Assertions.assertEquals("코온텐츠",createdArticle.getContent(),"content 불일치");
        Assertions.assertEquals(VisibleYnEnum.VISIBLE,createdArticle.getVisibleYn(),"visible 불일치");
    }

}