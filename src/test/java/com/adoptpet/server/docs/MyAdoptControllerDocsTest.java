package com.adoptpet.server.docs;


import com.adoptpet.server.adopt.dto.response.MyAdoptResponseDto;
import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.commons.security.config.CustomOAuth2UserService;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.user.controller.MyAdoptController;
import com.adoptpet.server.user.service.MemberService;
import com.adoptpet.server.user.service.MyAdoptService;
import com.adoptpet.testUser.WithMockCustomAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest(controllers = MyAdoptController.class)
public class MyAdoptControllerDocsTest extends RestDocsBasic{

    @MockBean
    MyAdoptService myAdoptService;

    @Test
    @DisplayName("나의 분양글 리스트 조회 테스트")
    @WithMockCustomAccount
    public void myAdopt() throws Exception {
        List<MyAdoptResponseDto> myAdoptList = List.of(
          new MyAdoptResponseDto(1, "강아지 분양해요", "너무 귀여워요", "코린이", 23, 3, LocalDateTime.now(), "/thumbnail"),
          new MyAdoptResponseDto(2, "페페 분양해요", "밥 많이 먹어요", "코더남", 32, 1, LocalDateTime.now(), "/pepe")
        );

        given(myAdoptService.myAdoptList(any(), any()))
                .willReturn(myAdoptList);

        mvc.perform(get("/mypage/adopt")
                        .headers(GenerateMockToken.getToken())
                        .param("status", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        ),
                        requestParameters(
                                parameterWithName("_csrf").ignored(),
                                parameterWithName("status").description("status")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("id"),
                                fieldWithPath("[].title").description("title"),
                                fieldWithPath("[].context").description("context"),
                                fieldWithPath("[].author").description("author"),
                                fieldWithPath("[].view").description("view"),
                                fieldWithPath("[].like").description("like"),
                                fieldWithPath("[].publishedAt").description("publishedAt"),
                                fieldWithPath("[].thumb").description("thumb")
                        )
                ));
    }
}
