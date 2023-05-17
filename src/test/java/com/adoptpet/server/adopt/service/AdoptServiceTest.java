package com.adoptpet.server.adopt.service;

import com.adoptpet.server.adopt.repository.AdoptBookmarkRepository;
import com.adoptpet.server.adopt.repository.AdoptImageRepository;
import com.adoptpet.server.adopt.repository.AdoptRepository;
import com.adoptpet.server.user.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdoptServiceTest {

    @Mock
    private AdoptRepository adoptRepository;
    @Mock
    private AdoptImageRepository adoptImageRepository;
    @Mock
    private AdoptBookmarkRepository adoptBookmarkRepository;
    @Mock
    private AdoptQueryService queryService;
    @Mock
    private MemberService memberService;

    @InjectMocks
    private AdoptService adoptService;

    @Test
    void findBySaleNo() {
    }

    @Test
    void deleteAdopt() {
    }

    @Test
    void insertAdopt() {
    }

    @Test
    void updateAdopt() {
    }

    @Test
    void updateAdoptImageSaleNo() {
    }

    @Test
    void insertAdoptBookmark() {
    }

    @Test
    void readAdopt() {
    }
}