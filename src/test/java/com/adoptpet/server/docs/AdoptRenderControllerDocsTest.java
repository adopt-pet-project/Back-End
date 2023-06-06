package com.adoptpet.server.docs;

import com.adoptpet.server.adopt.controller.AdoptRenderController;
import com.adoptpet.server.adopt.dto.response.AdoptRenderResponseDto;
import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.adopt.service.AdoptQueryService;
import com.adoptpet.server.commons.security.config.CustomOAuth2UserService;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.user.service.MemberService;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.*;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest(controllers = AdoptRenderController.class)
@MockBeans({
        @MockBean(MongoChatRepository.class),
        @MockBean(JwtUtil.class),
        @MockBean(MemberService.class),
        @MockBean(JpaMetamodelMappingContext.class),
        @MockBean(CustomOAuth2UserService.class)
})
public class AdoptRenderControllerDocsTest {

    @MockBean
    AdoptQueryService adoptQueryService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("분양글 랜더링용 리스트 조회 테스트")
    @WithMockCustomAccount
    public void getRenderAdoptList() throws Exception {
        List<AdoptRenderResponseDto> renderList = List.of(
               new AdoptRenderResponseDto(1, "댕댕이1", "1년 반", "강아지", 75.32f, 31.23f, "thumbnail1"),
               new AdoptRenderResponseDto(2, "댕댕이2", "5년 반", "강아지", 55.32f, 71.23f, "thumbnail13")
        );

        given(adoptQueryService.getAllAdoptList())
                .willReturn(renderList);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/adopt/render")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("adopt-render-list",
                        requestParameters(
                                parameterWithName("_csrf").ignored()
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("id"),
                                fieldWithPath("[].name").description("name"),
                                fieldWithPath("[].age").description("age"),
                                fieldWithPath("[].kind").description("kind"),
                                fieldWithPath("[].latitude").description("latitude"),
                                fieldWithPath("[].longitude").description("longitude"),
                                fieldWithPath("[].thumbnail").description("thumbnail"),
                                fieldWithPath("[].modal").description("modal")
                        )
                ));
    }
}
