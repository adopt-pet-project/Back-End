//package com.adoptpet.server.community.repository;
//
//
//import com.adoptpet.server.community.domain.BlindEnum;
//import com.adoptpet.server.community.domain.Community;
//import com.adoptpet.server.community.domain.LogicalDelEnum;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ActiveProfiles("test")
//@DataJpaTest
//class CommunityRepositoryTest {
//
//    @Autowired
//    private CommunityRepository communityRepository;
//
//    private static Community save;
//
//    @BeforeEach
//    void init(){
//        save = Community.builder()
//                .blindYn(BlindEnum.NORMAL)
//                .categoryNo(1)
//                .content("조회 테스트1")
//                .logicalDel(LogicalDelEnum.NORMAL)
//                .modId("testId")
//                .regId("testId")
//                .title("조회 테스트 체크")
//                .viewCount(0)
//                .build();
//    }
//
//
////    @DisplayName("게시글 조회 - QueryDSL")
////    @Test
////    void readArticleUseQDsl_Success(){
////        /**
////         * 테스트 시나리오
////         * - given : test - 게시글 1개 저장
////         *           h2   - 댓글 2개 저장, 회원 프로필 저장, 회원 저장, 게시글 좋아요 저장
////         * - when  : 기본키로 게시글 조회
////         * - then  : 조회된 값이 저장된 값과 일치하는지 테스트
////         **/
////        //given
////        Community savedCommunity = communityRepository.save(save);
////
////
////        //when
////        ArticleDetailInfo community = communityQDslRepository.findArticleDetail(savedCommunity.getArticleNo());
////
////        //then
////        assertEquals(save.getArticleNo(),community.getArticleNo(),"articleNo false");
////        assertEquals(save.getContent(),community.getContent(),"content false");
////        assertEquals(save.getTitle(),community.getTitle(),"title false");
////        assertEquals(save.getRegId(),community.getRegId(),"regId false");
////        assertEquals(save.getViewCount(),community.getView(),"view false");
////        assertEquals(save.getRegDate(),community.getRegDate(),"regDate false");
////        assertNotNull(community.getNickname(),"nick false");
////        assertNotNull(community.getLike(),"like false");
////        assertNotNull(community.getComment(),"comment false");
////        assertNotNull(community.getProfile(),"profile false");
////
////
////
////    }
//
//
//    @DisplayName("게시글 조회 - 기본")
//    @Test
//    void readArticle_Success(){
//        /**
//         * 테스트 시나리오
//         * - given : 게시글 1개 저장
//         * - when  : 기본키로 게시글 조회
//         * - then  : 조회된 값이 저장된 값과 일치하는지 테스트
//         **/
//
//        //given
//        Community savedCommunity = communityRepository.save(save);
//
//        //when
//        Community community = communityRepository.findById(savedCommunity.getArticleNo())
//                .orElseThrow();
//
//        //then
//        assertEquals(save.getContent(),community.getContent(),"content false");
//        assertEquals(save.getTitle(),community.getTitle(),"title false");
//        assertEquals(save.getRegId(),community.getRegId(),"regId false");
//
//    }
//}