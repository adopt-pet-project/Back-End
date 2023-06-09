package com.adoptpet.server.docs;

import com.adoptpet.server.adopt.controller.AdoptRenderController;
import com.adoptpet.server.adopt.dto.response.AdoptRenderResponseDto;
import com.adoptpet.server.adopt.service.AdoptQueryService;
import com.adoptpet.testUser.WithMockCustomAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

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
public class AdoptRenderControllerDocsTest extends RestDocsBasic{

    @MockBean
    AdoptQueryService adoptQueryService;

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

        mvc.perform(get("/adopt/render")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
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
