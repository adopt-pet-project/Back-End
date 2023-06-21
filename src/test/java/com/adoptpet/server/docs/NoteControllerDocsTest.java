package com.adoptpet.server.docs;

import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.commons.notification.service.NotificationService;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.community.controller.NoteController;
import com.adoptpet.server.community.domain.LogicalDelEnum;
import com.adoptpet.server.community.dto.NoteDto;
import com.adoptpet.server.community.dto.NoteHistoryDto;
import com.adoptpet.server.community.dto.request.SendNoteRequest;
import com.adoptpet.server.community.service.NoteService;
import com.adoptpet.server.docs.GenerateMockToken;
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
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NoteController.class)
class NoteControllerDocsTest extends RestDocsBasic {

    @MockBean
    private NoteService noteService;

    @Test
    @DisplayName("쪽지 - 전송")
    @WithMockCustomAccount
    void sendNote() throws Exception {

        SendNoteRequest requestDto = new SendNoteRequest(1,"쪽지 내용");
        String jsonString = mapper.writeValueAsString(requestDto);

        ResultActions result = mvc.perform(post("/note/send")
                        .headers(GenerateMockToken.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonString));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        requestFields(
                                fieldWithPath("targetId").type(NUMBER).description("상대 회원 고유번호"),
                                fieldWithPath("contents").type(STRING).description("보내는 쪽지 내용")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태코드")
                        )
                ));

    }

    @Test
    @DisplayName("쪽지 - 목록 조회")
    @WithMockCustomAccount
    void readNoteList() throws Exception{

        NoteDto noteDto = NoteDto.builder()
                .noteNo(1)
                .opponentNo(2)
                .nickName("닉네임")
                .content("하이")
                .mine(true)
                .regDate(LocalDateTime.now())
                .logicalDel(LogicalDelEnum.NORMAL)
                .build();

        List<NoteDto> noteDtoList = List.of(noteDto);

        given(noteService.readNoteList(any())).willReturn(noteDtoList);

        ResultActions result = mvc.perform(get("/note/list")
                .headers(GenerateMockToken.getToken())
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(NUMBER).description("쪽지 고유번호"),
                                fieldWithPath("[].targetId").type(NUMBER).description("상대방 고유번호"),
                                fieldWithPath("[].name").type(STRING).description("상대방 닉네임"),
                                fieldWithPath("[].contents").type(STRING).description("마지막 쪽지 내용"),
                                fieldWithPath("[].mine").type(BOOLEAN).description("마지막 쪽지 전송자 구분")
                                        .attributes(
                                                key("true").value("본인이 전송한 쪽지가 마지막일 경우"),
                                                key("false").value("상대방이 전송한 쪽지가 마지막일 경우")
                                        ),
                                fieldWithPath("[].publishedAt").type(STRING).description("마지막 쪽지 전송 일시"),
                                fieldWithPath("[].checked").type(BOOLEAN).description("마지막 전송된 쪽지 읽은 상태 분류")
                                        .attributes(
                                                key("true").value("읽었을 경우"),
                                                key("false").value("안 읽었을 경우")
                                        ),
                                fieldWithPath("[].deleteStatus").type(NUMBER).description("게시글 등록일")
                                        .attributes(
                                                key("0").value("정상"),
                                                key("1").value("논리 삭제")
                                        )
                        )
                ));
    }

    @Test
    @DisplayName("쪽지 - 대화 내역 조회")
    @WithMockCustomAccount
    void readNoteHistoryList() throws Exception{

        NoteHistoryDto historyDto = NoteHistoryDto.builder()
                .historyNo(1)
                .mine(true)
                .content("하이")
                .regDate(LocalDateTime.now())
                .logicalDel(LogicalDelEnum.NORMAL)
                .build();

        List<NoteHistoryDto> historyDtoList = List.of(historyDto);

        given(noteService.readNoteHistoryList(any(),eq(1))).willReturn(historyDtoList);

        ResultActions result = mvc.perform(get("/note/history/{id}",1)
                .headers(GenerateMockToken.getToken())
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        pathParameters(
                                parameterWithName("id").description("쪽지 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(NUMBER).description("쪽지 대화 내역 고유번호"),
                                fieldWithPath("[].mine").type(BOOLEAN).description("쪽지 전송자 구분")
                                        .attributes(
                                                key("true").value("본인이 전송한 쪽지일 경우"),
                                                key("false").value("상대방이 전송한 쪽지일 경우")
                                        ),
                                fieldWithPath("[].contents").type(STRING).description("내용"),
                                fieldWithPath("[].publishedAt").type(STRING).description("마지막 쪽지 전송 일시"),
                                fieldWithPath("[].deleteStatus").type(NUMBER).description("게시글 등록일")
                                        .attributes(
                                                key("0").value("정상"),
                                                key("1").value("논리 삭제")
                                        )
                        )
                ));
    }

    @Test
    @DisplayName("쪽지 - 읽음 처리")
    @WithMockCustomAccount
    void updateNote() throws Exception{
        ResultActions result = mvc.perform(patch("/note/checked/{id}",1)
                .headers(GenerateMockToken.getToken()));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        pathParameters(
                                parameterWithName("id").description("쪽지 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태코드")
                        )
                ));
    }

    @Test
    @DisplayName("쪽지 - 대화 내역 삭제")
    @WithMockCustomAccount
    void deleteHistory() throws Exception{
        ResultActions result = mvc.perform(delete("/note/history/{id}",1)
                .headers(GenerateMockToken.getToken()));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Access Token")
                        ),
                        pathParameters(
                                parameterWithName("id").description("쪽지 대화 내역 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태코드")
                        )
                ));
    }
}