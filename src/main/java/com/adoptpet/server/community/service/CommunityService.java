package com.adoptpet.server.community.service;

import com.adoptpet.server.commons.exception.CustomException;
import com.adoptpet.server.commons.exception.ErrorCode;
import com.adoptpet.server.commons.image.dto.ImageInfoDto;
import com.adoptpet.server.commons.notification.domain.NotifiTypeEnum;
import com.adoptpet.server.commons.notification.service.NotificationService;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.community.domain.*;
import com.adoptpet.server.community.dto.*;
import com.adoptpet.server.community.repository.*;
import com.adoptpet.server.community.service.mapper.CreateArticleMapper;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

import static com.adoptpet.server.commons.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityQDslRepository communityQDslRepository;
    private final CommunityImageRepository communityImageRepository;
    private final CategoryRepository categoryRepository;
    private final MemberService memberService;
    private final ArticleBookmarkRepository articleBookmarkRepository;
    private final ArticleHeartRepository articleHeartRepository;
    private final PopularArticleRepository popularArticleRepository;
    private final NotificationService notificationService;


    //== HOT 게시글 선정 스케줄러 ==//
//    @Scheduled(cron = "0 0 0/6 * * *",zone = "Asia/Seoul")
    public void selectionHotArticle(){
        log.info("====== 인기글(HOT) 스케줄 실행 ======");
        final LocalDateTime endAt = LocalDateTime.now();
        final LocalDateTime startAt = endAt.minusDays(1);

        //스케쥴 실행 일시부터 -1일까지 좋아요를 받은 게시글 조회
        List<TrendingArticleDto> articleOfDay
                = communityQDslRepository.findTrendingArticles(startAt, endAt);

        for(TrendingArticleDto trendingArticle : articleOfDay){
            final int articleNo = trendingArticle.getArticleNo();
            // 게시글 조회
            Community article = findByArticleNo(articleNo);
            // 인기 게시글 선정 내역이 없을 경우
            if(Objects.isNull(article.getPopularArticle())){
                // 좋아요가 특정 개수 이상일떄
                if(trendingArticle.getLikeCnt() >= 3){
                    // 인기 게시글 생성 후 저장
                    PopularArticle hotArticle = PopularArticle.createHotArticle(article);
                    popularArticleRepository.save(hotArticle);
                    // 게시글 소유자 조회
                    Member receiver = getMember(article.getRegId());

                    // 인기 게시글 선정 알림 전송
                    notificationService.send(
                            getManager(), receiver, NotifiTypeEnum.ARTICLE_HOT,
                            articleNo, article.getContent()
                    );
                }
            }
        }
    }

    //== WEEKLY 게시글 선정 스케줄러 ==//
    @Scheduled(cron = "59 59 23 * * *", // 매일 23시 59분 59초에 실행
            zone = "Asia/Seoul")
    public void selectionWeeklyArticle(){
        log.info("====== 인기글(WEEKLY) 스케줄 실행 ======");
        final LocalDateTime endAt = LocalDateTime.now();
        final LocalDateTime startAt = endAt.minusDays(7);
        //스케쥴 실행 일시부터 -7일까지 좋아요를 받은 게시글 조회
        List<TrendingArticleDto> articleOfWeekly
              = communityQDslRepository.findTrendingArticles(startAt, endAt);

        for(TrendingArticleDto trendingArticle : articleOfWeekly){
            final int articleNo = trendingArticle.getArticleNo();
            Community article = findByArticleNo(articleNo);
            PopularArticle popularArticle = article.getPopularArticle();
            // 인기 게시글 선정 내역이 있을 경우
            if(Objects.nonNull(popularArticle)){
                // 인기 게시글 선정된 내역이 HOT이고, 좋아요가 특정 개수 이상일떄
                boolean isHot = popularArticle.getStatus().equals(PopularEnum.HOT);
                if(isHot && trendingArticle.getLikeCnt() >= 5 ) {
                    // 인기 상태 변경 후 저장
                    popularArticle.updateStatusToWeekly();
                    popularArticleRepository.save(popularArticle);
                    // 게시글 소유자 조회
                    Member receiver = getMember(article.getRegId());
                    // 인기 게시글 선정 알림 전송
                    notificationService.send(
                            getManager(), receiver, NotifiTypeEnum.ARTICLE_WEEK,
                            articleNo, article.getContent()
                    );
                }
            }
        }
    }

    // 이메일로 회원 조회
    private Member getMember(String memberEmail) {
        return memberService.findByEmail(memberEmail).orElseThrow(ErrorCode::throwEmailNotFound);
    }

    private Member getManager(){
        return memberService.findByMemberNo(0);
    }

    /**
    * 게시글 목록의 인기글(HOT,WEEKLY) 조회
    **/
    @Transactional(readOnly = true)
    public Map<String,ArticleListDto> getTrendingArticleDayAndWeekly(){

        final LocalDateTime endAt = LocalDateTime.now();
        final LocalDateTime startAtOfDay = endAt.minusDays(1);
        final LocalDateTime startAtOfWeekly = endAt.minusWeeks(1);

        // HOT 인기 게시글 선정 내역 조회
        List<PopularArticle> hotArticle =
                popularArticleRepository.findArticleByPeriod(startAtOfDay, endAt, PopularEnum.HOT);
        // WEEKLY 인기 게시글 선정 내역 조회
        List<PopularArticle> weeklyArticle
                = popularArticleRepository.findArticleByPeriod(startAtOfWeekly, endAt, PopularEnum.WEEKLY);
        // 무작위 정렬
        Collections.shuffle(hotArticle);
        Collections.shuffle(weeklyArticle);

        Map<String,ArticleListDto> result = new HashMap<>();

        ArticleListDto articleDataOfHot = null;
        ArticleListDto articleDataOfWeekly = null;

        // HOT 게시글 선정내역으로 게시글 데이터 조회
        if(!hotArticle.isEmpty()){
            final Integer articleNoOfHot = hotArticle.get(0).getCommunity().getArticleNo();
            articleDataOfHot = communityQDslRepository.findArticleOneForList(articleNoOfHot);
        }

        // WEEKLY 게시글 선정내역으로 게시글 데이터 조회
        if(!weeklyArticle.isEmpty()){
            final Integer articleNoOfWeekly = weeklyArticle.get(0).getCommunity().getArticleNo();
            articleDataOfWeekly = communityQDslRepository.findArticleOneForList(articleNoOfWeekly);
        }

        result.put("weekly",articleDataOfWeekly);
        result.put("hot",articleDataOfHot);

        return result;
    }


    /**
     * @title 게시글 목록 조회 - 검색용
     **/
    @Transactional(readOnly = true)
    public List<ArticleListDto> readArticleList(String order, Integer pageNum, Integer option, String keyword){
        return communityQDslRepository.findArticleList(order,pageNum,option,keyword);
    }


    /**
     * @title 게시글 목록 조회 - 회원용
     **/
    @Transactional(readOnly = true)
    public List<ArticleListDto> readArticleList(String memberEmail){
        return communityQDslRepository.findArticleList(memberEmail);
    }


    /**
    * 게시글 유무 검증
    **/
    @Transactional(readOnly = true)
    public Community findByArticleNo(Integer articleNo){
        // 게시글 번호로 게시글을 조회하고, 조회되지 않을 경우 예외를 발생시킨다.
        Optional<Community> findCommunity = communityRepository.findById(articleNo);
        if(!findCommunity.isPresent()){
            throw new CustomException(ARTICLE_NOT_FOUND);
        }

        // 삭제된 게시글 조회 검증
        if(!findCommunity.get().getLogicalDel().equals(LogicalDelEnum.NORMAL)){
            throw new CustomException(DELETED_ARTICLE_BAD_REQUEST);
        }

        return findCommunity.get();
    }

    //== 카테고리 유무 검증 ==//
    @Transactional(readOnly = true)
    public void checkCategoryNo(Integer categoryNo) {
        Optional<Category> findCategory = categoryRepository.findById(categoryNo);
        if(!findCategory.isPresent()){
            throw new CustomException(CATEGORY_NOT_FOUND);
        }
    }

    /**
    * 게시글 수정
    **/
    @Transactional
    public Community updateArticle(ArticleDto articleDto, Integer articleNo){

        // 게시글 고유키로 게시글 검증
        Community community = findByArticleNo(articleNo);

        // 게시글 데이터 엔티티 저장
        community.updateArticleByMod(articleDto, SecurityUtils.getUser().getEmail());

        // 카테고리 번호 검증
        checkCategoryNo(community.getCategoryNo());

        // 이미지 업데이트
        updateImageByArticleNo(articleDto.getImage(),articleNo);

        // 게시글 업데이트
        Community updatedArticle = communityRepository.save(community);

        return updatedArticle;
    }


    /**
    * 게시글 상세 내용 조회
    **/
    @Transactional(readOnly = true)
    public ArticleDetailInfoDto readArticle(Integer articleNo, String accessToken, HttpServletRequest request, HttpServletResponse response){
        // 게시글 고유키로 게시글 검증
        findByArticleNo(articleNo);
        // 게시글 정보 조회
        ArticleDetailInfoDto articleDetail = communityQDslRepository.findArticleDetail(articleNo);
        // 조회 유저 검증 기본 값 지정
        articleDetail.addIsMine(false);
        // 엑세스 토큰 있을 시 조회한 유저가 게시글의 주인인지 확인
        if (StringUtils.hasText(accessToken)) {
            // 현재 게시글을 보려는 회원이 이 게시글을 작성한 작성자와 같은지 확인한다.
            boolean isMine = communityQDslRepository.isMine(SecurityUtils.getUser().getEmail(), articleNo);
            articleDetail.addIsMine(isMine);
        }
        // 이미지 URL 조회 -> 없을 시 null 반환
        List<ImageInfoDto> images = communityQDslRepository.findImageUrlByArticleNo(articleNo);
        // 이미지 URL 리스트 추가
        articleDetail.addImages(images);
        // 게시글 조회수 증가
        increaseCount(articleNo, request, response);
        // 조회된 게시글 정보 반환
        return articleDetail;
    }


    /**
    * 게시글 등록 메서드
     *  @param articleDto  : 게시글 생성 정보
     *  @return response     : 생성 완료된 게시글 정보
    **/
    @Transactional
    public ArticleDto insertArticle(ArticleDto articleDto){
        // CreateArticleMapper 인스턴스 생성
        final CreateArticleMapper createArticleMapper = CreateArticleMapper.INSTANCE;

        // DTO를 Entity로 매핑
        Community community = createArticleMapper.toEntity(articleDto);

        // 카테고리 번호 검증
        checkCategoryNo(community.getCategoryNo());

        // Community DB 저장
        Community saveArticle = communityRepository.save(community);

        // 이미지 업데이트
        ArticleImageDto[] images = articleDto.getImage();
        updateImageByArticleNo(articleDto.getImage(), saveArticle.getArticleNo());

        // 저장된 Community를 DTO로 변환
        ArticleDto response = createArticleMapper.toDTO(saveArticle);

        // 반환값에 null 대신 image 기입
        response.addImgNo(images);

        return response;
    }




    @Transactional
    public void updateImageByArticleNo(ArticleImageDto[] imageNoArr, Integer articleNo) {
        if (!Objects.isNull(imageNoArr)) {
            // 커뮤니티 이미지 테이블에 커뮤니티 게시글 고유키와 정렬 기준을 업데이트
            for (int i=0; i<imageNoArr.length; i++) {
                communityImageRepository.updateImagByArticleNo(articleNo, imageNoArr[i].getImageNo(), (i+1));
            }
        } else {
            communityImageRepository.updateAllByArticleNo(articleNo);
        }
    }
    
    
    /**
     * 게시글 논리 삭제
     */
    @Transactional
    public Community softDeleteArticle(Integer articleNo) {

        // 게시글 번호를 사용하여 해당 게시글을 조회한다.
        Community community = findByArticleNo(articleNo);
        // 게시글을 논리 삭제한다.
        community.deleteByLogicalDel(LogicalDelEnum.DELETE);
        // 게시글과 관련된 데이터인 북마크와 좋아요를 삭제한다.
        communityQDslRepository.deleteBookmark(community);
        communityQDslRepository.deleteArticleLike(community);
        // 게시글 이미지의 게시글 번호 컬럼을 비운다.(batch를 통해 자정에 삭제)
        communityImageRepository.updateAllByArticleNo(articleNo);
        // 변경된 게시글을 DB에 저장하여 업데이트한다.
        Community deletedCommunity = communityRepository.save(community);

        return deletedCommunity;
    }


    @Transactional
    public void insertArticleBookmark(SecurityUserDto dto, Integer articleNo) {

        // 게시글 조회
        Community community = findByArticleNo(articleNo);
        // 고유키로 Member 엔티티 조회
        Member member = memberService.findByMemberNo(dto.getMemberNo());
        // 회원 고유키와 게시글 고유키로 북마크 조회
        Optional<ArticleBookmark> findBookmark =
                articleBookmarkRepository.findByMemberNoAndArticleNo(member.getMemberNo(), community.getArticleNo());
        // 이미 관심글로 등록되어 있을 경우 충돌 예외 처리
        if(findBookmark.isPresent()) {
            throw new CustomException(DUPLICATE_BOOKMARK);
        }
        // 북마크 엔티티 생성
        ArticleBookmark createdBookmark = ArticleBookmark.createArticleBookmark(dto.getEmail(), member, community);
        // DB 저장
        articleBookmarkRepository.save(createdBookmark);
    }

    @Transactional
    public void deleteArticleBookmark(SecurityUserDto dto, Integer articleNo){

        // 회원 고유키와 게시글 고유키로 북마크 조회
        Optional<ArticleBookmark> findBookmark =
                articleBookmarkRepository.findByMemberNoAndArticleNo(dto.getMemberNo(), articleNo);
        // 등록된 관심글이 아닐 경우
        if(findBookmark.isEmpty()) {
          throw new CustomException(DUPLICATE_NOT_ADDITION_BOOKMARK);
        }
        // DB에서 제거
        articleBookmarkRepository.delete(findBookmark.get());
    }


    @Transactional
    public void increaseCount(Integer articleNo, HttpServletRequest request, HttpServletResponse response) {
        Cookie oldCookie = null;
        // 현재 브라우저의 쿠키를 전부 가져온다.
        Cookie[] cookies = request.getCookies();

        // 쿠키가 있을 경우 실행
        if (Objects.nonNull(cookies)) {
            // 반복문을 돌면서 communityView라는 이름을 가진 쿠키가 있을 경우 oldCookie에 값을 담아준다.
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("communityView")) {
                    oldCookie = cookie;
                }
            }
        }

        // communityView 쿠키가 null이 아닐경우 실행한다.
        if (Objects.nonNull(oldCookie)) {
            // 현재 게시글 번호를 값으로 포함한 쿠키가 없을 경우 실행한다.
            if (!oldCookie.getValue().contains("[" + articleNo + "]")) {
                // 조회수를 1 증가시킨다.
                communityRepository.increaseCount(articleNo);
                // 현재 쿠키의 값에 조회한 게시글 번호를 이어서 저장해준다.
                oldCookie.setValue(oldCookie.getValue() + "_[" + articleNo + "]");
                // 이 쿠키는 모든 요청에 같이 전달되도록 설정
                oldCookie.setPath("/");
                // 쿠키의 유효기간을 하루로 설정
                oldCookie.setMaxAge(60 * 60 * 24);
                // 응답 객체(response)에 쿠키를 셋팅해준다.
                response.addCookie(oldCookie);
            }
        } else {
            // oldCookie의 값이 없다면 현재 조회한 게시글이 하나도 없는 상태이므로 조회 수 카운트를 올려준다.
            communityRepository.increaseCount(articleNo);
            // 쿠키를 새로 생성하면서 현재 게시글의 번호를 값으로 넣어준다.
            Cookie newCookie = new Cookie("communityView", "[" + articleNo + "]");
            // 이 쿠키는 모든 요청에 같이 전달되도록 설정
            newCookie.setPath("/");
            // 쿠키의 유효기간을 하루로 설정
            newCookie.setMaxAge(60 * 60 * 24);
            // 응답 객체(response)에 쿠키를 셋팅해준다.
            response.addCookie(newCookie);
        }

    }

    @Transactional
    public Integer insertArticleHeart(SecurityUserDto userDto, Integer articleNo) {

        // member 엔티티 조회
        Member member = memberService.findByMemberNo(userDto.getMemberNo());
        // community 엔티티 조회
        Community community = findByArticleNo(articleNo);

        Optional<ArticleHeart> findHeart
                = articleHeartRepository.findByCommunityAndMember(community, member);
        // 해당 멤버가 해당 게시글에 좋아요를 이미 눌렀는지 확인
        if(findHeart.isPresent()){
            throw new CustomException(ErrorCode.DUPLICATE_HEART);
        }
        // ArticleHeart 엔티티 생성
        ArticleHeart articleHeart = ArticleHeart.createArticleHeart(userDto.getEmail(), community, member);
        // 좋아요를 저장
        articleHeartRepository.save(articleHeart);
        // 업데이트된 좋아요 개수 반환
        Community resultArticle = findByArticleNo(articleNo);
        return resultArticle.getHeartCnt();
    }

    @Transactional
    public Integer deleteArticleHeart(SecurityUserDto dto, Integer articleNo) {
        // member 엔티티 조회
        Member member = memberService.findByMemberNo(dto.getMemberNo());
        // community 엔티티 조회
        Community community = findByArticleNo(articleNo);

        Optional<ArticleHeart> findHeart = articleHeartRepository.findByCommunityAndMember(community, member);
        // 해당 멤버가 해당 게시글의 좋아요를 안가졌는지 확인
        if(findHeart.isEmpty()){
            throw new CustomException(DUPLICATE_NOT_HEART);
        }
        // 좋아요를 제거
        articleHeartRepository.deleteByHeartNo(findHeart.get().getHeartNo());

        Community resultArticle = findByArticleNo(articleNo);

        // 업데이트된 좋아요 개수 반환
        return resultArticle.getHeartCnt();
    }


}
