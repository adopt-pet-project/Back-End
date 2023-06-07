package com.adoptpet.server.docs;

import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.commons.image.dto.ImageInfoDto;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.community.controller.CommunityController;
import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.dto.ArticleDetailInfoDto;
import com.adoptpet.server.community.dto.ArticleDto;
import com.adoptpet.server.community.dto.ArticleImageDto;
import com.adoptpet.server.community.dto.request.RegisterArticleRequest;
import com.adoptpet.server.community.dto.request.UpdateArticleRequest;
import com.adoptpet.server.community.service.CommentService;
import com.adoptpet.server.community.service.CommunityService;
import com.adoptpet.server.user.service.MemberService;
import com.adoptpet.testUser.WithMockCustomAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommunityController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class})
@MockBeans({
        @MockBean(JwtUtil.class),
        @MockBean(MongoChatRepository.class),
        @MockBean(MemberService.class),
        @MockBean(CommentService.class),
        @MockBean(JpaMetamodelMappingContext.class),
})
class CommunityControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommunityService communityService;

    @Test
    @DisplayName("게시글 등록")
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

        ResultActions result = this.mockMvc.perform(
                post("/community/article")
                        .with(csrf())
//                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("article-register",
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
    @DisplayName("게시글 상세 조회")
    @WithMockCustomAccount
    void readArticle() throws Exception {

        ArticleDetailInfoDto articleDetailInfoDto = ArticleDetailInfoDto.builder()
                .articleNo(1)
                .mine(true)
                .title("Sample Article")
                .memberNo(123)
                .nickname("john")
                .profile("profile.jpg")
                .view(10)
                .like(5)
                .comment(3)
                .regDate(LocalDateTime.now())
                .content("This is a sample article.")
                .images(Collections.singletonList(ImageInfoDto.builder()
                    .imageUrl("image.jpg")
                    .imageNo(51)
                    .build()))
                .build();


        given(communityService.readArticle(eq(1), anyString(), any(), any()))
                .willReturn(articleDetailInfoDto);

        ResultActions result = this.mockMvc.perform(
                get("/community/article/{articleNo}", articleDetailInfoDto.getArticleNo())
                                .with(csrf())
                                .headers(GenerateMockToken.getToken())
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

        result.andDo(document("article-read",
                pathParameters(
                        parameterWithName("articleNo").description("게시글 고유번호")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("Access token").optional()
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The article number"),
                        fieldWithPath("mine").type(JsonFieldType.BOOLEAN).description("Indicates if the article belongs to the current user"),
                        subsectionWithPath("header").description("Article header information"),
                        subsectionWithPath("context").description("Article context information"),
                        fieldWithPath("header.title").type(JsonFieldType.STRING).description("Title of the article"),
                        fieldWithPath("header.authorId").type(JsonFieldType.NUMBER).description("ID of the author"),
                        fieldWithPath("header.username").type(JsonFieldType.STRING).description("Username of the author"),
                        fieldWithPath("header.profile").type(JsonFieldType.STRING).description("Profile image URL of the author"),
                        fieldWithPath("header.view").type(JsonFieldType.NUMBER).description("Number of views"),
                        fieldWithPath("header.like").type(JsonFieldType.NUMBER).description("Number of likes"),
                        fieldWithPath("header.comment").type(JsonFieldType.NUMBER).description("Number of comments"),
                        fieldWithPath("header.publishedAt").type(JsonFieldType.STRING).description("Date and time of publication"),
                        fieldWithPath("context.context").type(JsonFieldType.STRING).description("Content of the article"),
                        fieldWithPath("context.imageList").type(JsonFieldType.ARRAY).description("List of images in the article"),
                        fieldWithPath("context.imageList[].imageUrl").type(JsonFieldType.STRING).description("URL of the image")
                )
        ));
    }

    @Test
    @DisplayName("게시글 내용 수정")
    @WithMockCustomAccount
    void articleModification() throws Exception {

        UpdateArticleRequest request = UpdateArticleRequest.builder()
                .categoryNo(1)
                .title("수정 제목")
                .content("수정 내용")
                .build();

        String jsonString = objectMapper.writeValueAsString(request);

        ResultActions result = this.mockMvc.perform(patch("/article/{articleNo}", 1)
                        .headers(GenerateMockToken.getToken())
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk());

        result.andDo(document("article-modification",
                pathParameters(
                        parameterWithName("articleNo").description("The article's number")
                ),
                requestFields(
                        fieldWithPath("categoryId").description("The category ID of the article"),
                        fieldWithPath("title").description("The updated title of the article"),
                        fieldWithPath("content").description("The updated content of the article"),
                        fieldWithPath("imageList").description("The list of article images").optional()
                ),
                responseFields(
                        fieldWithPath("status").type(NUMBER).description("상태코드")
                )
        ));
    }


    @Test
    void heartRegistration() {

    }

    @Test
    void heartDeletion() {
    }

    @Test
    void readCommentList() {
    }

    @Test
    void commentRegistration() {
    }

    @Test
    void commentModification() {
    }

    @Test
    void commentDeletion() {
    }

    @Test
    void readArticleList() {
    }







    @Test
    void articleDeletion() {
    }

    @Test
    void articleBookmarkAddition() {
    }

    @Test
    void articleBookmarkDeletion() {
    }
}