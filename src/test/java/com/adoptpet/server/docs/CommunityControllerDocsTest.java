package com.adoptpet.server.docs;

import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.commons.image.dto.ImageInfoDto;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.community.controller.CommunityController;
import com.adoptpet.server.community.domain.BlindEnum;
import com.adoptpet.server.community.domain.LogicalDelEnum;
import com.adoptpet.server.community.dto.*;
import com.adoptpet.server.community.dto.request.*;
import com.adoptpet.server.community.service.CommentService;
import com.adoptpet.server.community.service.CommunityService;
import com.adoptpet.server.user.service.MemberService;
import com.adoptpet.testUser.WithMockCustomAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommunityController.class)
class CommunityControllerDocsTest extends RestDocsBasic{

    @MockBean
    private CommunityService communityService;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("게시글 - 등록")
    @WithMockCustomAccount
    void articleRegistration() throws Exception {

        ArticleImageDto articleImageDto = new ArticleImageDto(100, "test@url.com");
        ArticleImageDto[] articleImageList = new ArticleImageDto[1];
        articleImageList[0] = articleImageDto;

        RegisterArticleRequest request = RegisterArticleRequest.builder()
                .categoryNo(1)
                .title("제목")
                .content("내용")
                .image(articleImageList)
                .thumbnail("testThumb@url.com")
                .build();

        ResultActions result = mvc.perform(
                post("/community/article")
                        .headers(GenerateMockToken.getToken())
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("categoryId").type(NUMBER).description("카테고리 번호"),
                                fieldWithPath("title").type(STRING).description("게시글 제목"),
                                fieldWithPath("context").type(STRING).description("게시글 내용"),
                                fieldWithPath("imageList.[].id").type(NUMBER).description("이미지 번호"),
                                fieldWithPath("imageList.[].url").type(STRING).description("이미지 URL"),
                                fieldWithPath("thumbnail").type(STRING).description("게시글 썸네일")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("상태코드")
                        )
                 ));
    }
    @Test
    @DisplayName("게시글 - 상세 내용 조회")
    @WithMockCustomAccount
    void readArticle() throws Exception {

        ArticleDetailInfoDto articleDetailInfoDto = ArticleDetailInfoDto.builder()
                .articleNo(1)
                .mine(true)
                .title("제목")
                .memberNo(123)
                .nickname("닉네임")
                .profile("profile.jpg")
                .view(10)
                .like(5)
                .comment(3)
                .regDate(LocalDateTime.now())
                .content("내용")
                .images(Collections.singletonList(ImageInfoDto.builder()
                    .imageUrl("image.jpg")
                    .imageNo(51)
                    .build()))
                .build();


        given(communityService.readArticle(eq(1), anyString()))
                .willReturn(articleDetailInfoDto);

        ResultActions result = mvc.perform(
                get("/community/article/{id}", articleDetailInfoDto.getArticleNo())
                                .headers(GenerateMockToken.getToken())
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

        result.andDo(restDocs.document(
                pathParameters(
                        parameterWithName("id").description("게시글 고유번호")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("Access token").optional()
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 고유번호"),
                        fieldWithPath("mine").type(JsonFieldType.BOOLEAN).description("요청 회원 검증 속성"),
                        fieldWithPath("header").type(OBJECT).description("정보 파트"),
                        fieldWithPath("header.title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("header.authorId").type(JsonFieldType.NUMBER).description("작성자 고유번호"),
                        fieldWithPath("header.username").type(JsonFieldType.STRING).description("작성자 닉네임"),
                        fieldWithPath("header.profile").type(JsonFieldType.STRING).description("작성자 프로필"),
                        fieldWithPath("header.view").type(JsonFieldType.NUMBER).description("조회수"),
                        fieldWithPath("header.like").type(JsonFieldType.NUMBER).description("좋아요수"),
                        fieldWithPath("header.comment").type(JsonFieldType.NUMBER).description("댓글수"),
                        fieldWithPath("header.publishedAt").type(JsonFieldType.STRING).description("작성일"),
                        fieldWithPath("context").type(OBJECT).description("내용 파트"),
                        fieldWithPath("context.context").type(JsonFieldType.STRING).description("내용"),
                        fieldWithPath("context.imageList[].imageNo").type(NUMBER).description("이미지 고유번호"),
                        fieldWithPath("context.imageList[].imageUrl").type(JsonFieldType.STRING).description("이미지 주소")
                )
        ));
    }

    @Test
    @DisplayName("게시글 - 상세 내용 수정")
    @WithMockCustomAccount
    void articleModification() throws Exception {

        ArticleImageDto imageDto = ArticleImageDto.builder().imageNo(1).imageUrl("test@url.com").build();
        ArticleImageDto[] imageListDto = new ArticleImageDto[]{imageDto};


        UpdateArticleRequest request = UpdateArticleRequest.builder()
                .categoryNo(1)
                .title("수정 제목")
                .content("수정 내용")
                .image(imageListDto)
                .build();

        String jsonString = mapper.writeValueAsString(request);

        ResultActions result = mvc.perform(patch("/community/article/{id}", 1)
                        .headers(GenerateMockToken.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonString));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                requestHeaders(
                        headerWithName(AUTHORIZATION).description("Access Token")
                ),
                pathParameters(
                        parameterWithName("id").description("게시글 고유번호")
                ),
                requestFields(
                        fieldWithPath("categoryId").type(NUMBER).description("카테고리 고유번호"),
                        fieldWithPath("title").type(STRING).description("수정 제목"),
                        fieldWithPath("context").type(STRING).description("수정 내용"),
                        fieldWithPath("imageList.[].id").type(NUMBER).description("이미지 번호"),
                        fieldWithPath("imageList.[].url").type(STRING).description("이미지 URL")
                ),
                responseFields(
                        fieldWithPath("status").type(NUMBER).description("응답 상태코드")
                )
        ));
    }


    @Test
    @DisplayName("게시글 - 삭제")
    @WithMockCustomAccount
    void articleDeletion() throws Exception {

        ResultActions result = mvc.perform(delete("/community/article/{id}", 1)
                .headers(GenerateMockToken.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));


        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        pathParameters(
                                parameterWithName("id").description("게시글 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태코드")
                        )
                ));
    }


    @Test
    @DisplayName("게시글 - 목록 조회")
    @WithMockCustomAccount
    void readArticleList() throws Exception {


        ArticleListDto articleListDtoOne = new ArticleListDto(1, "제목", "내용", "닉네임",
                0, 1, 1, LocalDateTime.now(), "thumb@url.com");
        ArticleListDto articleListDtoTwo = new ArticleListDto(2, "제목", "내용", "닉네임",
                0, 1, 1, LocalDateTime.now(), "thumb@url.com");

        List<ArticleListDto> articleList = List.of(articleListDtoOne,articleListDtoTwo);

        Map<String,ArticleListDto> trendingArticle = Map.of(
                "hot", articleListDtoOne,
                "weekly",articleListDtoTwo
        );

        given(communityService.getTrendingArticleDayAndWeekly())
                .willReturn(trendingArticle);

        given(communityService.readArticleList(anyString(),any(),any(),anyString()))
                .willReturn(articleList);

        ResultActions result = mvc.perform(get("/community/list/{order}", "recent")
                .headers(GenerateMockToken.getToken())
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        pathParameters(
                                parameterWithName("order").description("조회 조건")
                                        .attributes(
                                                key("recent").value("최신순 정렬"),
                                                key("like").value("추천순 정렬")
                                        ),
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("option").description("검색 조건")
                                        .attributes(key("1").value("제목"),
                                                key("2").value("내용"),
                                                key("3").value("제목 + 내용")
                                        ).optional(),
                                parameterWithName("keyword").description("검색 키워드").optional()
                        ),
                        responseFields(
                                fieldWithPath("hot").type(OBJECT).description("HOT 게시글 정보").optional(),
                                fieldWithPath("hot.id").type(NUMBER).description("게시글 고유번호"),
                                fieldWithPath("hot.title").type(STRING).description("게시글 제목"),
                                fieldWithPath("hot.context").type(STRING).description("게시글 내용"),
                                fieldWithPath("hot.author").type(STRING).description("게시글 작성자 닉네임"),
                                fieldWithPath("hot.view").type(NUMBER).description("조회수"),
                                fieldWithPath("hot.comment").type(NUMBER).description("댓글수"),
                                fieldWithPath("hot.like").type(NUMBER).description("좋아요수"),
                                fieldWithPath("hot.publishedAt").type(STRING).description("게시글 등록일"),
                                fieldWithPath("hot.thumbnail").type(STRING).description("썸네일 이미지 URL"),
                                fieldWithPath("weekly").type(OBJECT).description("WEEKLY 게시글 정보").optional(),
                                fieldWithPath("weekly.id").type(NUMBER).description("게시글 고유번호"),
                                fieldWithPath("weekly.title").type(STRING).description("게시글 제목"),
                                fieldWithPath("weekly.context").type(STRING).description("게시글 내용"),
                                fieldWithPath("weekly.author").type(STRING).description("게시글 작성자 닉네임"),
                                fieldWithPath("weekly.view").type(NUMBER).description("조회수"),
                                fieldWithPath("weekly.comment").type(NUMBER).description("댓글수"),
                                fieldWithPath("weekly.like").type(NUMBER).description("좋아요수"),
                                fieldWithPath("weekly.publishedAt").type(STRING).description("게시글 등록일"),
                                fieldWithPath("weekly.thumbnail").type(STRING).description("썸네일 이미지 URL"),
                                fieldWithPath("list[]").type(ARRAY).description("게시글 정보 리스트").optional(),
                                fieldWithPath("list[].id").type(NUMBER).description("게시글 고유번호"),
                                fieldWithPath("list[].title").type(STRING).description("게시글 제목"),
                                fieldWithPath("list[].context").type(STRING).description("게시글 내용"),
                                fieldWithPath("list[].author").type(STRING).description("게시글 작성자 닉네임"),
                                fieldWithPath("list[].view").type(NUMBER).description("조회수"),
                                fieldWithPath("list[].comment").type(NUMBER).description("댓글수"),
                                fieldWithPath("list[].like").type(NUMBER).description("좋아요수"),
                                fieldWithPath("list[].publishedAt").type(STRING).description("게시글 등록일"),
                                fieldWithPath("list[].thumbnail").type(STRING).description("썸네일 이미지 URL").optional()
                        )
                ));
    }



    @Test
    @DisplayName("댓글 - 등록")
    @WithMockCustomAccount
    void commentRegistration() throws Exception {

        RegisterCommentRequest request = new RegisterCommentRequest(1,2,"댓글 내용");

        String requestJson = mapper.writeValueAsString(request);

        ResultActions result = mvc.perform(post("/community/comment")
                .headers(GenerateMockToken.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestJson));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        requestFields(
                                fieldWithPath("boardId").type(NUMBER).description("게시글 고유번호"),
                                fieldWithPath("parentId").type(NUMBER).description("부모 댓글 번호").optional(),
                                fieldWithPath("context").type(STRING).description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태코드")
                        )
                ));
    }


    @Test
    @DisplayName("댓글 - 내용 수정")
    @WithMockCustomAccount
    void commentModification() throws Exception {

        ModifyCommentRequest request = new ModifyCommentRequest(1,"수정 내용");

        String jsonString = mapper.writeValueAsString(request);

        ResultActions result = mvc.perform(patch("/community/comment")
                .headers(GenerateMockToken.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonString));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        requestFields(
                                fieldWithPath("id").type(NUMBER).description("댓글 고유번호"),
                                fieldWithPath("context").type(STRING).description("수정 내용")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태코드")
                        )
                ));
    }


    @Test
    @DisplayName("댓글 - 삭제")
    @WithMockCustomAccount
    void commentDeletion() throws Exception {
        ResultActions result = mvc.perform(delete("/community/comment/{id}", 1)
                .headers(GenerateMockToken.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        pathParameters(
                                parameterWithName("id").description("댓글 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태코드")
                        )
                ));
    }


    @Test
    @DisplayName("댓글 - 목록 조회")
    @WithMockCustomAccount
    void readCommentList() throws Exception {


        CommentListDto childComment = CommentListDto.builder()
                .type(CommentTypeEnum.COMMENT)
                .mine(true)
                .commentNo(1)
                .commentHeart(0)
                .nickname("닉네임")
                .memberNo(1)
                .content("내용")
                .profile("url.com")
                .regDate(LocalDateTime.now())
                .blindYn(BlindEnum.NORMAL)
                .logicalDel(LogicalDelEnum.NORMAL)
                .childComment(null)
                .build();

        List<CommentListDto> commentDtoList = List.of(CommentListDto.builder()
                .type(CommentTypeEnum.COMMENT)
                .mine(true)
                .commentNo(1)
                .commentHeart(0)
                .nickname("닉네임")
                .memberNo(1)
                .content("내용")
                .profile("url.com")
                .regDate(LocalDateTime.now())
                .blindYn(BlindEnum.NORMAL)
                .logicalDel(LogicalDelEnum.NORMAL)
                .childComment(List.of(childComment))
                .build());

        given(commentService.readCommentList(eq(1),anyString()))
                .willReturn(commentDtoList);

        ResultActions result = mvc.perform(get("/community/comment/{id}", 1)
                .headers(GenerateMockToken.getToken())
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        pathParameters(
                                parameterWithName("id").description("게시글 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("[].type").type(NUMBER).description("댓글 분류")
                                        .attributes(
                                                key("0").value("부모 댓글"),
                                                key("1").value("자식 댓글(대댓글)")
                                        ),
                                fieldWithPath("[].mine").type(BOOLEAN).description("작성자 확인"),
                                fieldWithPath("[].id").type(NUMBER).description("댓글 고유번호"),
                                fieldWithPath("[].author").type(STRING).description("작성자 닉네임"),
                                fieldWithPath("[].authorId").type(NUMBER).description("작성자 고유번호"),
                                fieldWithPath("[].context").type(STRING).description("댓글 내용"),
                                fieldWithPath("[].profile").type(STRING).description("작성자 프로필 이미지 URL"),
                                fieldWithPath("[].publishedAt").type(STRING).description("게시글 등록일"),
                                fieldWithPath("[].like").type(NUMBER).description("좋아요수"),
                                fieldWithPath("[].deleteStatus").type(NUMBER).description("댓글 삭제 상태")
                                        .attributes(
                                                key("0").value("정상"),
                                                key("1").value("작성자 삭제 처리"),
                                                key("2").value("회원 탈퇴로 인한 삭제 처리")
                                        ),
                                fieldWithPath("[].blindStatus").type(NUMBER).description("댓글 노출 상태")
                                        .attributes(
                                                key("0").value("정상"),
                                                key("1").value("신고로 blind 처리")
                                        ),
                                fieldWithPath("[].comments[]").type(ARRAY).description("대댓글 목록").optional(),
                                fieldWithPath("[].comments[].type").type(NUMBER).description("댓글 분류"),
                                fieldWithPath("[].comments[].mine").type(BOOLEAN).description("작성자 확인"),
                                fieldWithPath("[].comments[].id").type(NUMBER).description("댓글 고유번호"),
                                fieldWithPath("[].comments[].author").type(STRING).description("작성자 닉네임"),
                                fieldWithPath("[].comments[].authorId").type(NUMBER).description("작성자 고유번호"),
                                fieldWithPath("[].comments[].context").type(STRING).description("댓글 내용"),
                                fieldWithPath("[].comments[].profile").type(STRING).description("작성자 프로필 이미지 URL"),
                                fieldWithPath("[].comments[].publishedAt").type(STRING).description("게시글 등록일"),
                                fieldWithPath("[].comments[].like").type(NUMBER).description("좋아요수"),
                                fieldWithPath("[].comments[].deleteStatus").type(NUMBER).description("댓글 삭제 상태"),
                                fieldWithPath("[].comments[].blindStatus").type(NUMBER).description("댓글 노출 상태")
                        )
                ));

    }


    @Test
    @DisplayName("게시글/댓글 좋아요 - 추가")
    @WithMockCustomAccount
    void heartRegistration() throws Exception {

        Integer like = 0;

        given(communityService.insertArticleHeart(any(),eq(1))).willReturn(like);
        given(commentService.insertCommentHeart(any(),eq(1))).willReturn(like);

        String jsonString = "{ \"target\": \"article\" , \"id\" : 1 }";

        ResultActions result = mvc.perform(post("/community/heart")
                .headers(GenerateMockToken.getToken())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
        );

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        requestFields(
                                fieldWithPath("target").type(STRING).description("분류")
                                        .attributes(
                                                key("article").value("게시글"),
                                                key("comment").value("댓글")
                                        ),
                                fieldWithPath("id").type(NUMBER).description("해당 글의 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태코드"),
                                fieldWithPath("data").type(NUMBER).description("갱신된 좋아요 수")
                        )
                ));
    }

    @Test
    @DisplayName("게시글/댓글 좋아요 - 제거")
    @WithMockCustomAccount
    void heartDeletion() throws Exception {

        Integer like = 0;

        given(communityService.deleteArticleHeart(any(),eq(1))).willReturn(like);
        given(commentService.deleteCommentHeart(any(),eq(1))).willReturn(like);

        ResultActions result = mvc.perform(delete("/community/heart/{target}/{id}","article",1)
                .headers(GenerateMockToken.getToken())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        pathParameters(
                                parameterWithName("target").description("대상 글 분류")
                                        .attributes(
                                                key("article").value("게시글"),
                                                key("comment").value("댓글")
                                        ),
                                parameterWithName("id").description("해당 글의 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태코드"),
                                fieldWithPath("data").type(NUMBER).description("갱신된 좋아요 수")
                        )
                ));
    }


    @Test
    @DisplayName("북마크 - 추가")
    @WithMockCustomAccount
    void articleBookmarkAddition() throws Exception {

        ResultActions result = mvc.perform(post("/community/bookmark/{id}",1)
                .headers(GenerateMockToken.getToken())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        pathParameters(
                                parameterWithName("id").description("해당 글의 고유키")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태코드")
                        )
                ));
    }

    @Test
    @DisplayName("북마크 - 제거")
    @WithMockCustomAccount
    void articleBookmarkDeletion() throws Exception {

        ResultActions result = mvc.perform(delete("/community/bookmark/{id}",1)
                .headers(GenerateMockToken.getToken())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        pathParameters(
                                parameterWithName("id").description("해당 글의 고유키")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태코드")
                        )
                ));
    }
}