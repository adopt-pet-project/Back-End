package com.adoptpet.server.docs;

import com.adoptpet.server.adopt.controller.AdoptController;
import com.adoptpet.server.adopt.domain.Gender;
import com.adoptpet.server.adopt.dto.request.AdoptImageRequestDto;
import com.adoptpet.server.adopt.dto.request.AdoptRequestDto;
import com.adoptpet.server.adopt.dto.request.AdoptUpdateRequestDto;
import com.adoptpet.server.adopt.dto.response.AdoptDetailResponseDto;
import com.adoptpet.server.adopt.dto.response.AdoptImageResponseDto;
import com.adoptpet.server.adopt.dto.response.AdoptResponseDto;
import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.adopt.service.AdoptQueryService;
import com.adoptpet.server.adopt.service.AdoptService;
import com.adoptpet.server.commons.security.config.CustomOAuth2UserService;
import com.adoptpet.server.commons.security.service.JwtUtil;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.*;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest(controllers = AdoptController.class)
public class AdoptControllerDocsTest extends RestDocsBasic {


    @MockBean
    AdoptQueryService adoptQueryService;

    @MockBean
    AdoptService adoptService;



    @Test
    @DisplayName("분양글 등록 테스트")
    @WithMockCustomAccount
    public void createAdopt() throws Exception{
        AdoptRequestDto requestDto = AdoptRequestDto.builder()
                .title("드래곤 분양합니다")
                .age("1년 반")
                .address("서울 은평구")
                .gender(Gender.MAN)
                .content("드래곤 분양합니다.")
                .latitude(7.1932f)
                .longitude(8.1234f)
                .species("뫁티즈")
                .name("미미")
                .kind("dog")
                .image(new AdoptImageRequestDto[]{
                        AdoptImageRequestDto.builder()
                                .imgNo(2)
                                .imgUrl("/test").build(),
                        AdoptImageRequestDto.builder()
                                .imgNo(3)
                                .imgUrl("/test2").build()
                })
                .build();

        String requestJson = createStringJson(requestDto);

        mvc.perform(post("/adopt").headers(GenerateMockToken.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        ),
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
                                fieldWithPath("image").description("image"),
                                fieldWithPath("image[].imgNo").description("imgNo"),
                                fieldWithPath("image[].imgUrl").description("imgUrl")
                        ),
                        responseFields(
                                fieldWithPath("status").description("status")
                        )
                        ));
    }

    @Test
    @DisplayName("분양글 수정 테스트")
    @WithMockCustomAccount
    public void modifyAdopt() throws Exception {
        AdoptUpdateRequestDto requestDto = AdoptUpdateRequestDto.builder()
                .title("드래곤 분양해요")
                .age("1년 반")
                .address("서울 은평구")
                .gender(Gender.MAN)
                .content("드래곤 분양합니다.")
                .latitude(7.1932f)
                .longitude(8.1234f)
                .species("뫁티즈")
                .name("미미")
                .image(new AdoptImageRequestDto[]{
                        AdoptImageRequestDto.builder()
                                .imgNo(2)
                                .imgUrl("/test").build(),
                        AdoptImageRequestDto.builder()
                                .imgNo(3)
                                .imgUrl("/test2").build()
                })
                .build();

        String jsonString = createStringJson(requestDto);

        mvc.perform(patch("/adopt").headers(GenerateMockToken.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        ),
                        requestFields(
                                fieldWithPath("title").description("title"),
                                fieldWithPath("content").description("content"),
                                fieldWithPath("address").description("address"),
                                fieldWithPath("age").description("age"),
                                fieldWithPath("gender").description("gender"),
                                fieldWithPath("species").description("species"),
                                fieldWithPath("name").description("name"),
                                fieldWithPath("latitude").description("latitude"),
                                fieldWithPath("longitude").description("longitude"),
                                fieldWithPath("image").description("image"),
                                fieldWithPath("image[].imgNo").description("imgNo"),
                                fieldWithPath("image[].imgUrl").description("imgUrl")
                        ),
                        responseFields(
                                fieldWithPath("status").description("status")
                        )
                        ));
    }

    @Test
    @DisplayName("관심 분양글 등록 테스트")
    @WithMockCustomAccount
    public void createAdoptBookmark() throws Exception{
        mvc.perform(post("/adopt/bookmark/{saleNo}", 1)
                        .headers(GenerateMockToken.getToken()))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                          headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        ),
                        pathParameters(
                                parameterWithName("saleNo").description("saleNo")
                        ),
                        responseFields(
                                fieldWithPath("status").description("status")
                        )
                        ));
    }

    @Test
    @DisplayName("관심 분양글 삭제 테스트")
    @WithMockCustomAccount
    public void removeAdoptBookmark() throws Exception{
        mvc.perform(delete("/adopt/bookmark/{saleNo}", 1)
                        .headers(GenerateMockToken.getToken())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        ),
                        pathParameters(
                                parameterWithName("saleNo").description("saleNo")
                        ),
                        responseFields(
                                fieldWithPath("status").description("status")
                        )
                ));
    }

    @Test
    @DisplayName("분양글 삭제 테스트")
    @WithMockCustomAccount
    public void deleteAdopt() throws Exception {
        mvc.perform(delete("/adopt/{saleNo}", 1)
                .headers(GenerateMockToken.getToken()))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        ),
                        pathParameters(
                                parameterWithName("saleNo").description("saleNo")
                        ),
                        responseFields(
                                fieldWithPath("status").description("status")
                        )
                        ));
    }

    @Test
    @DisplayName("분양글 리스트 조회 테스트")
    @WithMockCustomAccount
    public void getAdoptList() throws Exception {
        List<AdoptResponseDto> adoptResponse = List.of(
                new AdoptResponseDto(1, "드래곤분양", "서울 은평구", 3, 4, LocalDateTime.now(),
                        "/png1", "몰티즈", 2),
                new AdoptResponseDto(2, "허스키 분양", "서울 마포구", 7, 8, LocalDateTime.now(),
                        "/png2", "시베리안 허스키", 3)
        );

        given(adoptQueryService.selectAdoptList(null, null, null, null))
                .willReturn(adoptResponse);

        mvc.perform(get("/adopt")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("adopt-list",
                        requestParameters(
                                parameterWithName("saleNo").description("saleNo").optional(),
                                parameterWithName("keyword").description("keyword").optional(),
                                parameterWithName("option").description("option").optional(),
                                parameterWithName("filter").description("filter").optional()
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("id"),
                                fieldWithPath("[].title").description("title"),
                                fieldWithPath("[].address").description("address"),
                                fieldWithPath("[].bookmark").description("bookmark"),
                                fieldWithPath("[].chat").description("chat"),
                                fieldWithPath("[].publishedAt").description("publishedAt"),
                                fieldWithPath("[].thumbnail").description("thumbnail"),
                                fieldWithPath("[].species").description("species"),
                                fieldWithPath("[].status").description("status")
                        )
                        ));
    }

    @Test
    @DisplayName("분양글 상세조회 테스트")
    @WithMockCustomAccount
    public void getAdoptDetail() throws Exception {
        AdoptDetailResponseDto.Coords coords = new AdoptDetailResponseDto.Coords(75.34f, 25.26f, "서울 양천구");
        AdoptDetailResponseDto.Header header = new AdoptDetailResponseDto.Header("강아지 분양해요", 0, LocalDateTime.now());
        AdoptDetailResponseDto.Context context = new AdoptDetailResponseDto.Context("허숙희 분양해요", 2, 8);
        AdoptDetailResponseDto.Metadata metadata = new AdoptDetailResponseDto.Metadata(Gender.MAN, "1년 반", "갸륵이", "시베리안 허스키");
        AdoptDetailResponseDto.Author author = new AdoptDetailResponseDto.Author(1, "분양", "/profile.jpg", "서울 양천구");
        List<AdoptImageResponseDto> images = List.of(new AdoptImageResponseDto(1, "/img1"),
                                                        new AdoptImageResponseDto(2, "/img2"));

        AdoptDetailResponseDto responseDto = new AdoptDetailResponseDto(1, images, true, coords, header, metadata, context, author);

        given(adoptService.readAdopt(any(), any()))
                .willReturn(responseDto);

        mvc.perform(get("/adopt/{saleNo}", 1)
                .headers(GenerateMockToken.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                            requestHeaders(
                                    headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken").optional()
                            ),
                            pathParameters(
                                    parameterWithName("saleNo").description("saleNo")
                            ),
                            responseFields(
                                    fieldWithPath("id").description("id"),
                                    fieldWithPath("imageList").description("imageList"),
                                    fieldWithPath("imageList[].imgNo").description("imgNo"),
                                    fieldWithPath("imageList[].imgUrl").description("imgUrl"),
                                    fieldWithPath("mine").description("mine"),
                                    fieldWithPath("coords").description("coords"),
                                    fieldWithPath("coords.latitude").description("latitude"),
                                    fieldWithPath("coords.longitude").description("longitude"),
                                    fieldWithPath("coords.address").description("address"),
                                    fieldWithPath("header").description("header"),
                                    fieldWithPath("header.title").description("title"),
                                    fieldWithPath("header.status").description("status"),
                                    fieldWithPath("header.publishedAt").description("publishedAt"),
                                    fieldWithPath("metadata").description("metadata"),
                                    fieldWithPath("metadata.gender").description("gender"),
                                    fieldWithPath("metadata.age").description("age"),
                                    fieldWithPath("metadata.name").description("name"),
                                    fieldWithPath("metadata.species").description("species"),
                                    fieldWithPath("context").description("context"),
                                    fieldWithPath("context.context").description("context"),
                                    fieldWithPath("context.bookmark").description("bookmark"),
                                    fieldWithPath("context.chat").description("chat"),
                                    fieldWithPath("author").description("author"),
                                    fieldWithPath("author.id").description("id"),
                                    fieldWithPath("author.username").description("username"),
                                    fieldWithPath("author.profile").description("profile"),
                                    fieldWithPath("author.address").description("address")
                            )
                        ));

    }
}
