package com.adoptpet.server.docs;

import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.commons.security.config.CustomOAuth2UserService;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.user.controller.AuthController;
import com.adoptpet.server.user.service.MemberService;
import com.adoptpet.server.user.service.RefreshTokenService;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.*;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerDocsTest extends RestDocsBasic{

    @MockBean
    RefreshTokenService tokenService;

    @Test
    @DisplayName("로그아웃 테스트")
    @WithMockCustomAccount
    void logout() throws Exception {

        mvc.perform(post("/token/logout")
                        .headers(GenerateMockToken.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        ),
                        responseFields(
                                fieldWithPath("status").description("status")
                        )
                ));
    }

    @Test
    @DisplayName("access Token 재발급 테스트")
    @WithMockCustomAccount
    void refresh() throws Exception {

        given(tokenService.republishAccessToken(any()))
                .willReturn("newAccessToken");

        mvc.perform(post("/token/refresh")
                        .headers(GenerateMockToken.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        ),
                        responseFields(
                                fieldWithPath("status").description("status"),
                                fieldWithPath("accessToken").description("accessToken")
                        )
                ));
    }
}
