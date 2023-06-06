package com.adoptpet.server.docs;

import com.adoptpet.server.adopt.controller.ChatController;
import com.adoptpet.server.adopt.dto.chat.Message;
import com.adoptpet.server.adopt.dto.request.ChatDisconnectDto;
import com.adoptpet.server.adopt.dto.request.ChatRequestDto;
import com.adoptpet.server.adopt.dto.response.ChatResponseDto;
import com.adoptpet.server.adopt.dto.response.ChatRoomResponseDto;
import com.adoptpet.server.adopt.dto.response.ChattingHistoryResponseDto;
import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.adopt.service.ChatRoomService;
import com.adoptpet.server.adopt.service.ChatService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.*;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest(controllers = ChatController.class)
@MockBeans({
        @MockBean(MongoChatRepository.class),
        @MockBean(JwtUtil.class),
        @MockBean(MemberService.class),
        @MockBean(JpaMetamodelMappingContext.class),
        @MockBean(CustomOAuth2UserService.class)
})
public class ChatControllerDocsTest {

    @MockBean
    ChatService chatService;

    @MockBean
    ChatRoomService chatRoomService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("채팅방 생성 테스트")
    @WithMockCustomAccount
    void createChatRoom() throws Exception {
        ChatRequestDto chatRequestDto = new ChatRequestDto(1, 3);
        String requestJson = objectMapper.writeValueAsString(chatRequestDto);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/chatroom")
                        .headers(GenerateMockToken.getToken())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andDo(document("chatroom-create",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        ),
                        requestFields(
                                fieldWithPath("saleNo").description("saleNo"),
                                fieldWithPath("createMember").description("createMember")
                        ),
                        responseFields(
                                fieldWithPath("status").description("status")
                        )
                ));
    }

    @Test
    @DisplayName("메시지 전송 후 콜백 테스트")
    @WithMockCustomAccount
    void sendNotification() throws Exception {
        Message requestMessage = Message.builder()
                .contentType("text")
                .chatNo(1)
                .saleNo(2)
                .content("Hello! World!")
                .build();

        Message responseMessage = Message.builder()
                .contentType("text")
                .chatNo(1)
                .saleNo(2)
                .content("Hello! World!")
                .id("fas2-dfad-2dsf-dsfa")
                .senderName("rexDev")
                .senderNo(1)
                .sendTime(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli())
                .readCount(0)
                .senderName("dev@Dev.com")
                .senderEmail("rexDev@Dev.com")
                .build();

        String requestJson = objectMapper.writeValueAsString(requestMessage);
        when(chatService.sendNotificationAndSaveMessage(any())).thenReturn(responseMessage);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/chatroom/notification")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andDo(document("chat-callback",
                        requestFields(
                                fieldWithPath("id").description("id").ignored(),
                                fieldWithPath("chatNo").description("chatNo"),
                                fieldWithPath("contentType").description("contentType"),
                                fieldWithPath("content").description("content"),
                                fieldWithPath("senderName").description("senderName").ignored(),
                                fieldWithPath("senderNo").description("senderNo").ignored(),
                                fieldWithPath("saleNo").description("saleNo"),
                                fieldWithPath("sendTime").description("sendTime").ignored(),
                                fieldWithPath("readCount").description("readCount").ignored(),
                                fieldWithPath("senderEmail").description("senderEmail").ignored()
                        ),
                        responseFields(
                                fieldWithPath("id").description("id"),
                                fieldWithPath("chatNo").description("chatNo"),
                                fieldWithPath("contentType").description("contentType"),
                                fieldWithPath("content").description("content"),
                                fieldWithPath("senderName").description("senderName"),
                                fieldWithPath("senderNo").description("senderNo"),
                                fieldWithPath("saleNo").description("saleNo"),
                                fieldWithPath("sendTime").description("sendTime"),
                                fieldWithPath("readCount").description("readCount"),
                                fieldWithPath("senderEmail").description("senderEmail")
                        )
                ));
    }

    @Test
    @DisplayName("채팅방 접속 끊기 테스트")
    @WithMockCustomAccount
    void disconnectChat() throws Exception {
        ChatDisconnectDto chatDisconnectDto = new ChatDisconnectDto(1, "rexDev@Dev.com");
        String requestJson = objectMapper.writeValueAsString(chatDisconnectDto);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/chatroom")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andDo(document("chatroom-delete",
                        requestFields(
                                fieldWithPath("chatRoomNo").description("chatRoomNo"),
                                fieldWithPath("email").description("email")
                        ),
                        responseFields(
                                fieldWithPath("status").description("status")
                        )
                ));
    }

    @Test
    @DisplayName("채팅방 리스트 조회 테스트")
    @WithMockCustomAccount
    void chatRoomList() throws Exception {
        ChatRoomResponseDto.Participant participant = new ChatRoomResponseDto.Participant("user2", "/image2");
        ChatRoomResponseDto.LatestMessage latestMessage = ChatRoomResponseDto.LatestMessage.builder()
                .context("안녕하세요 오랜만이네요:)")
                .sendAt(LocalDateTime.now())
                .build();
        ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto(1, 3, 4, 12, "강아지 분양 받아요"
                                                                                , LocalDateTime.now(), participant);
        chatRoomResponseDto.setUnReadCount(10L);
        chatRoomResponseDto.setLatestMessage(latestMessage);

        ChatRoomResponseDto.Participant participant2 = new ChatRoomResponseDto.Participant("user3", "/image3");
        ChatRoomResponseDto.LatestMessage latestMessage2 = ChatRoomResponseDto.LatestMessage.builder()
                .context("오랜만이네요:)")
                .sendAt(LocalDateTime.now())
                .build();
        ChatRoomResponseDto chatRoomResponseDto2 = new ChatRoomResponseDto(2, 5, 23, 43, "드래곤 분양 받아요"
                , LocalDateTime.now(), participant2);
        chatRoomResponseDto2.setUnReadCount(30L);
        chatRoomResponseDto2.setLatestMessage(latestMessage2);

        List<ChatRoomResponseDto> chatRoomlist = List.of(chatRoomResponseDto, chatRoomResponseDto2);

        given(chatService.getChatList(any(), any())).willReturn(chatRoomlist);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/chatroom")
                        .headers(GenerateMockToken.getToken())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("chatroom-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        ),
                        requestParameters(
                                parameterWithName("_csrf").ignored(),
                                parameterWithName("saleNo").description("saleNo").optional()
                        ),
                        responseFields(
                                fieldWithPath("[].chatNo").description("chatNo"),
                                fieldWithPath("[].createMember").description("createMember"),
                                fieldWithPath("[].joinMember").description("joinMember"),
                                fieldWithPath("[].saleNo").description("saleNo"),
                                fieldWithPath("[].saleTitle").description("saleTitle"),
                                fieldWithPath("[].regDate").description("regDate"),
                                fieldWithPath("[].participant").description("participant"),
                                fieldWithPath("[].participant.username").description("username"),
                                fieldWithPath("[].participant.profile").description("profile"),
                                fieldWithPath("[].latestMessage").description("latestMessage"),
                                fieldWithPath("[].latestMessage.context").description("context"),
                                fieldWithPath("[].latestMessage.sendAt").description("sendAt"),
                                fieldWithPath("[].unReadCount").description("unreadCount")
                        )
                        ));
    }

    @Test
    @DisplayName("채팅 리스트 조회 테스트")
    @WithMockCustomAccount
    void chattingList() throws Exception {
        ChatResponseDto chat1 = new ChatResponseDto("iderf-23fd-dfasdf", 2, 3, "rexdev", "image", "/image2",
                LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli(),
                1, true
                );

        ChatResponseDto chat2 = new ChatResponseDto("adsf-23fd-dfasdff", 8, 1, "dev1", "text", "hello!",
                LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli(),
                0, true
        );

        ChatResponseDto chat3 = new ChatResponseDto("aasdf-23fd-adsf2", 9, 4, "pro1", "text", "good!",
                LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli(),
                1, false
        );

        List<ChatResponseDto> chatList = List.of(chat1, chat2, chat3);
        ChattingHistoryResponseDto chattingHistoryResponseDto = new ChattingHistoryResponseDto("rexDev@Dev.com", chatList);

        given(chatService.getChattingList(any(), any())).willReturn(chattingHistoryResponseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/chatroom/{roomNo}", 1)
                        .headers(GenerateMockToken.getToken())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("chatting-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        ),
                        pathParameters(
                          parameterWithName("roomNo").description("roomNo")
                        ),
                        requestParameters(
                                parameterWithName("_csrf").ignored()
                        ),
                        responseFields(
                                fieldWithPath("email").description("email"),
                                fieldWithPath("chatList").description("chatList"),
                                fieldWithPath("chatList[].id").description("id"),
                                fieldWithPath("chatList[].chatRoomNo").description("chatRoomNo"),
                                fieldWithPath("chatList[].senderNo").description("senderNo"),
                                fieldWithPath("chatList[].senderName").description("senderName"),
                                fieldWithPath("chatList[].contentType").description("contentType"),
                                fieldWithPath("chatList[].content").description("content"),
                                fieldWithPath("chatList[].sendDate").description("sendDate"),
                                fieldWithPath("chatList[].readCount").description("readCount"),
                                fieldWithPath("chatList[].mine").description("mine")
                        )
                ));
    }


}
