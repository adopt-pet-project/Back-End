package com.adoptpet.server.docs;

import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.commons.security.config.CustomOAuth2UserService;
import com.adoptpet.server.commons.security.config.SecurityConfig;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.community.controller.CommunityController;
import com.adoptpet.server.community.dto.ArticleImageDto;
import com.adoptpet.server.community.dto.request.RegisterArticleRequest;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommunityController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@MockBeans({
        @MockBean(CommunityService.class),
        @MockBean(JwtUtil.class),
        @MockBean(MongoChatRepository.class),
        @MockBean(MemberService.class),
        @MockBean(CommentService.class),
        @MockBean(JpaMetamodelMappingContext.class),
})
class CommunityControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

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


        FieldDescriptor[] imageList = new FieldDescriptor[] {
                fieldWithPath("id").description("이미지 번호"),
                fieldWithPath("url").description("이미지 URL") };

        result.andExpect(status().isOk())
                .andDo(document("article-read",
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
    void readArticle() {
    }



    @Test
    void articleModification() {
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