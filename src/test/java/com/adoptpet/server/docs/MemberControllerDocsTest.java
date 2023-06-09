package com.adoptpet.server.docs;

import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.commons.security.config.CustomOAuth2UserService;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.user.controller.MemberController;
import com.adoptpet.server.user.dto.request.MemberModifyRequest;
import com.adoptpet.server.user.dto.request.RegisterDto;
import com.adoptpet.server.user.dto.response.MemberResponseDto;
import com.adoptpet.server.user.service.MemberService;
import com.adoptpet.testUser.WithMockCustomAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest(controllers = MemberController.class)
public class MemberControllerDocsTest extends RestDocsBasic{

    @MockBean
    MemberService memberService;


    @Test
    @DisplayName("회원가입 테스트")
    @WithMockCustomAccount
    public void register() throws Exception{
        RegisterDto registerDto = RegisterDto.builder()
                .provider("GOOGLE")
                .email("dev@Dev.com")
                .nickname("개발자2")
                .address("서울 마포구")
                .build();

        String requestJson = createStringJson(registerDto);

        mvc.perform(post("/member")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("provider").description("provider"),
                                fieldWithPath("email").description("email"),
                                fieldWithPath("address").description("address"),
                                fieldWithPath("nickname").description("nickname"),
                                fieldWithPath("imgUrl").description("imgUrl").optional(),
                                fieldWithPath("imgNo").description("imgNo").optional()
                        ),
                        responseFields(
                                fieldWithPath("status").description("status")
                        )
                ));
    }

    @Test
    @DisplayName("회원 조회 테스트")
    @WithMockCustomAccount
    public void userInfo() throws Exception {
        MemberResponseDto.Activity activity = new MemberResponseDto.Activity(6, 4, 2);
        MemberResponseDto memberResponseDto = new MemberResponseDto(1, "/image", "rexDev", "서울 마포구", activity);

        given(memberService.findMemberInfo(any())).willReturn(memberResponseDto);

        mvc.perform(get("/member/{id}", 1)
                        .headers(GenerateMockToken.getToken())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        ),
                        pathParameters(
                                parameterWithName("id").description("id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id"),
                                fieldWithPath("profile").description("profile"),
                                fieldWithPath("name").description("name"),
                                fieldWithPath("location").description("location"),
                                fieldWithPath("activity").description("activity"),
                                fieldWithPath("activity.document").description("document"),
                                fieldWithPath("activity.comment").description("comment"),
                                fieldWithPath("activity.sanction").description("sanction")
                        )
                ));

    }

    @Test
    @DisplayName("회원정보 수정 테스트")
    @WithMockCustomAccount
    public void modifyMember() throws Exception{
        MemberModifyRequest.Image image = new MemberModifyRequest.Image("/image2", 2);
        MemberModifyRequest modifyRequest = new MemberModifyRequest("코린이", image);

        String requestJson = createStringJson(modifyRequest);

        mvc.perform(patch("/member")
                        .headers(GenerateMockToken.getToken())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        ),
                        requestFields(
                                fieldWithPath("name").description("name").optional(),
                                fieldWithPath("image").description("image").optional(),
                                fieldWithPath("image.imageUrl").description("imageUrl").optional(),
                                fieldWithPath("image.imageKey").description("imageKey").optional()
                        ),
                        responseFields(
                                fieldWithPath("status").description("status")
                        )
                ));
    }

    @Test
    @DisplayName("닉네임 중복 여부 조회 테스트")
    @WithMockCustomAccount
    public void validateNickname() throws Exception {


        given(memberService.isDuplicated(any())).willReturn(true);

        mvc.perform(get("/member/validate")
                        .param("nickname", "코린이")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestParameters(
                                parameterWithName("_csrf").ignored(),
                                parameterWithName("nickname").description("nickname")
                        ),
                        responseFields(
                                fieldWithPath("duplicated").description("duplicated")
                        )
                ));

    }

    @Test
    @DisplayName("회원탈퇴 테스트")
    @WithMockCustomAccount
    public void removeUser() throws Exception{

        mvc.perform(delete("/member")
                        .headers(GenerateMockToken.getToken())
                        .with(csrf())
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
}
