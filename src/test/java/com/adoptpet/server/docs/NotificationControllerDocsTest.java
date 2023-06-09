package com.adoptpet.server.docs;

import com.adoptpet.server.commons.notification.controller.NotificationController;
import com.adoptpet.server.commons.notification.domain.NotifiTypeEnum;
import com.adoptpet.server.commons.notification.dto.NotificationResponse;
import com.adoptpet.server.commons.notification.service.NotificationService;
import com.adoptpet.testUser.WithMockCustomAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationController.class)
class NotificationControllerDocsTest extends RestDocsBasic{

    @MockBean
    private NotificationService notificationService;

    private static final String REQUEST_MAPPING = "/notification";

    @Test
    @DisplayName("알림 - SSE 연결")
    @WithMockCustomAccount
    void connect() throws Exception {

        ResultActions result = mvc.perform(get(REQUEST_MAPPING + "/connect")
                .headers(GenerateMockToken.getToken())
                .header("Last-Event-ID", "lastEventIdValue")
                .accept(MediaType.TEXT_EVENT_STREAM_VALUE)
        );

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token"),
                                headerWithName("Last-Event-ID").description("마지막 발생한 SSE ID")
                        ),
                        responseHeaders(
                                headerWithName("X-Accel-Buffering").description("X-Accel-Buffering header")
                                        .attributes(key("no").value("Nginx 버퍼링 옵션을 비활성화"))
                        )
                ));
    }

    @Test
    @DisplayName("알림 - 회원 알림 조회")
    @WithMockCustomAccount
    void notifications() throws Exception {

        NotificationResponse response = NotificationResponse.builder()
                .id(1L)
                .name("닉네임")
                .type(NotifiTypeEnum.COMMENT)
                .content("알림 내용")
                .read(false)
                .del(false)
                .publishedAt(LocalDateTime.now())
                .url("/board/1")
                .build();

        List<NotificationResponse> responseList = List.of(response);

        given(notificationService.findAllById(any())).willReturn(responseList);

        ResultActions result = mvc.perform(get(REQUEST_MAPPING + "/all")
                .headers(GenerateMockToken.getToken())
                .accept(MediaType.APPLICATION_JSON));


        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(NUMBER).description("알림 고유번호"),
                                fieldWithPath("[].type").type(STRING).description("알림 분류")
                                        .attributes(
                                                key("comment").value("회원 게시글에 댓글이 작성되었을 경우"),
                                                key("note").value("쪽지를 받았을 경우"),
                                                key("documentHot").value("HOT 게시글로 선정되었을 경우"),
                                                key("documentWeek").value("WEEKLY 게시글로 선정되었을 경우"),
                                                key("recomment").value("회원 댓글에 대댓글이 작성되었을 경우"),
                                                key("chat").value("채팅을 받았을 경우")
                                                ),
                                fieldWithPath("[].content").type(STRING).description("알림 내용"),
                                fieldWithPath("[].url").type(STRING).description("알림 redirect URL"),
                                fieldWithPath("[].publishedAt[]").type(ARRAY).description("알림 생성 일시"),
                                fieldWithPath("[].name").type(STRING).description("알림 발신지에 해당하는 회원의 닉네임"),
                                fieldWithPath("[].checked").type(BOOLEAN).description("알림 읽음 상태 구분")
                                        .attributes(
                                                key("true").value("읽은 상태"),
                                                key("false").value("안읽은 상태")
                                        ),
                                fieldWithPath("[].del").type(BOOLEAN).description("알림 삭제 상태")
                                        .attributes(
                                                key("true").value("삭제 처리"),
                                                key("false").value("정상")
                                        )
                        )
                ));

    }

    @Test
    @DisplayName("알림 - 읽음 처리")
    @WithMockCustomAccount
    void readNotification() throws Exception {

        ResultActions result = mvc.perform(patch(REQUEST_MAPPING + "/checked/{id}",1)
                .headers(GenerateMockToken.getToken()));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        pathParameters(
                                parameterWithName("id").description("알림 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태코드")
                        )
                ));
    }

    @Test
    @DisplayName("알림 - 삭제")
    @WithMockCustomAccount
    void deleteNotification() throws Exception {

        Long[] idList = {1L,2L,3L,4L};

        String jsonString = "{\"idList\":" + Arrays.toString(idList) + "}";

        ResultActions result = mvc.perform(post(REQUEST_MAPPING)
                .headers(GenerateMockToken.getToken())
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태코드")
                        )
                ));
    }
}