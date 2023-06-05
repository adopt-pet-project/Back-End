package com.adoptpet.server.docs;

import com.adoptpet.server.adopt.controller.AdoptController;
import com.adoptpet.server.adopt.domain.Gender;
import com.adoptpet.server.adopt.dto.request.AdoptRequestDto;
import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.adopt.service.AdoptQueryService;
import com.adoptpet.server.adopt.service.AdoptService;
import com.adoptpet.server.commons.security.config.CustomOAuth2UserService;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.user.service.MemberService;
import com.adoptpet.testUser.WithMockCustomAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest(controllers = AdoptController.class)
@MockBeans({
        @MockBean(AdoptService.class),
        @MockBean(AdoptQueryService.class),
        @MockBean(MongoChatRepository.class),
        @MockBean(JwtUtil.class),
        @MockBean(MemberService.class),
        @MockBean(JpaMetamodelMappingContext.class),
        @MockBean(CustomOAuth2UserService.class)
})
public class AdoptControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("분양글 등록 테스트")
    @WithMockCustomAccount
    public void createAdopt() throws Exception{
        AdoptRequestDto requestDto = AdoptRequestDto.builder()
                .title("드래곤 분양띠")
                .age("1년 반")
                .address("서울 은평구")
                .gender(Gender.MAN)
                .content("드래곤 분양합니다.")
                .latitude(7.1932f)
                .longitude(8.1234f)
                .species("뫁티즈")
                .name("미미")
                .kind("dog")
                .build();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/adopt")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andDo(document("adopt-create",
                        requestFields(
                            fieldWithPath("title").description("title"),
                                fieldWithPath("content").description("content"),
                                fieldWithPath("address").description("address"),
                                fieldWithPath("age").description("age"),
                                fieldWithPath("gender").description("gender"),
                                fieldWithPath("kind").description("kind"),
                                fieldWithPath("species").description("species"),
                                fieldWithPath("name").description("name"),
                                fieldWithPath("latitude").description("latitude"),
                                fieldWithPath("longitude").description("longitude"),
                                fieldWithPath("image").description("image")
                        ),
                        responseFields(
                            fieldWithPath("status").description("status")
                        )
                        ));
    }
}
