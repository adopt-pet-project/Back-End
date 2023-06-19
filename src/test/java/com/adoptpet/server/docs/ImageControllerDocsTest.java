package com.adoptpet.server.docs;

import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.commons.image.controller.ImageController;
import com.adoptpet.server.commons.image.dto.ImageInfoDto;
import com.adoptpet.server.commons.image.service.AwsS3Service;
import com.adoptpet.server.commons.security.service.JwtUtil;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ImageController.class)
class ImageControllerDocsTest extends RestDocsBasic{

    @MockBean
    private AwsS3Service awsS3Service;

    private static final String REQUEST_MAPPING = "/api/image";

    @Test
    @DisplayName("이미지 - 업로드")
    @WithMockCustomAccount
    void uploadImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "<<IMAGE_DATA>>".getBytes(StandardCharsets.UTF_8)
        );

        ImageInfoDto imageInfoDto = ImageInfoDto.builder()
                .imageNo(1)
                .imageUrl("/test-image.jpg")
                .build();

        String type = "community";
        String email = "test@example.com";

        given(awsS3Service.upload(eq(file),eq(type),eq(email),anyString())).willReturn(imageInfoDto);

        MockMultipartFile typePart
                = new MockMultipartFile("type", "", "application/json", type.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile emailPart
                = new MockMultipartFile("email", "", "application/json", email.getBytes(StandardCharsets.UTF_8));

        ResultActions result = mvc.perform(
                multipart(REQUEST_MAPPING)
                .file(file)
                .file(typePart)
                .file(emailPart)
                .headers(GenerateMockToken.getToken())
        );

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        requestParts(
                                partWithName("file").description("이미지 파일"),
                                partWithName("type").description("이미지 분류 속성"),
                                partWithName("email").description("화원 이메일")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태코드"),
                                fieldWithPath("data.id").type(NUMBER).description("이미지 고유번호"),
                                fieldWithPath("data.url").type(STRING).description("AWS S3 이미지 URL")
                        )
                ));
    }

    @Test
    @DisplayName("이미지 - 삭제")
    @WithMockCustomAccount
    void deleteImage() throws Exception {

        String type = "community";
        Integer id = 1;
        String resultString = "AWS S3 - Success";

        given(awsS3Service.delete(eq(type),eq(id))).willReturn(resultString);

        ResultActions result = mvc.perform(
                RestDocumentationRequestBuilders.delete(REQUEST_MAPPING + "/{type}/{id}",type,id)
                    .headers(GenerateMockToken.getToken())
        );

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        pathParameters(
                                parameterWithName("type").description("이미지 속성 분류"),
                                parameterWithName("id").description("이미지 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태코드"),
                                fieldWithPath("data").type(STRING).description("삭제 결과")
                        )
                ));
    }
}