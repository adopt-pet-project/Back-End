package com.adoptpet.server.commons.exception;

import com.adoptpet.server.commons.support.StatusResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalCatcher {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    protected ResponseEntity<StatusResponseDto> catchException() {
        return ResponseEntity.internalServerError().body(StatusResponseDto.addStatus(500));
    }
}
