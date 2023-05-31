package com.adoptpet.server.community.controller;


import com.adoptpet.server.commons.support.StatusResponseDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.community.dto.NoteDto;
import com.adoptpet.server.community.dto.request.SendNoteRequest;
import com.adoptpet.server.community.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {


    private final NoteService noteService;


    //== 응답 status 200 반환 ==//
    private static ResponseEntity<StatusResponseDto> success() {
        return ResponseEntity.ok(StatusResponseDto.success());
    }

    private static ResponseEntity<StatusResponseDto> success(Object responseData) {
        return ResponseEntity.ok(StatusResponseDto.success(responseData));
    }

    @PostMapping("/send")
    public ResponseEntity<StatusResponseDto> sendNote(@Valid @RequestBody SendNoteRequest request){

        noteService.sendNote(SecurityUtils.getUser(), request.getReceiverNo(), request.getContent());

        return success();
    }


    @GetMapping("/list")
    public ResponseEntity<List<NoteDto>> readNoteList(){
        return ResponseEntity.ok(noteService.readNoteList(SecurityUtils.getUser()));
    }


//    @GetMapping("/history/{noteNo}")
//    public ResponseEntity<List<NoteHistoryListResponse>> readNoteHistoryList(){
//        return success();
//    }

    @PatchMapping("/checked/{noteNo}")
    public ResponseEntity<StatusResponseDto> updateNote(@PathVariable("noteNo") Integer noteNo) {

        noteService.updateNoteHistory(noteNo);

        return success();
    }


}
