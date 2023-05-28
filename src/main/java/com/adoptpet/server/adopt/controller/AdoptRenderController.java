package com.adoptpet.server.adopt.controller;

import com.adoptpet.server.adopt.dto.response.AdoptRenderResponseDto;
import com.adoptpet.server.adopt.service.AdoptQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdoptRenderController {

    private final AdoptQueryService adoptQueryService;


    @GetMapping("/adopt/render")
    public ResponseEntity<List<AdoptRenderResponseDto>> getRenderAdoptList() {
        List<AdoptRenderResponseDto> adoptList = adoptQueryService.getAllAdoptList();
        return ResponseEntity.ok(adoptList);
    }


}
