package com.adoptpet.server.docs;


import com.adoptpet.server.adopt.mongo.MongoChatRepository;
import com.adoptpet.server.adopt.repository.ChatRoomRepository;
import com.adoptpet.server.adopt.service.AdoptQueryService2;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.user.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@MockBeans({
        @MockBean(JwtUtil.class),
        @MockBean(MongoChatRepository.class),
        @MockBean(MemberService.class),
        @MockBean(JpaMetamodelMappingContext.class),
        @MockBean(ChatRoomRepository.class),
        @MockBean(AdoptQueryService2.class)
})
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfiguration.class)
public class RestDocsBasic {

    @Autowired
    MockMvc mvc;

    @Autowired
    RestDocumentationResultHandler restDocs;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider provider){
        this.mvc= MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(MockMvcResultHandlers.print())
                .alwaysDo(restDocs)
                .addFilters(new CharacterEncodingFilter("UTF-8",true))
                .build();
    }


    protected String createStringJson(Object dto) throws JsonProcessingException {
        return mapper.writeValueAsString(dto);
    }

}
