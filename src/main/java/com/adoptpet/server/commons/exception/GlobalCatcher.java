package com.adoptpet.server.commons.exception;

import com.adoptpet.server.commons.support.StatusResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalCatcher {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    protected ResponseEntity<StatusResponseDto> catchException() {
        return ResponseEntity.internalServerError().body(StatusResponseDto.addStatus(500));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(MissingServletRequestPartException ex){

        String error = "누락된 필수 요청 값이 있습니다.";

        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),error);

        return ResponseEntity.badRequest().body(response);
    }

    //== 커스텀 예외 발생시 ==//
    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        log.error("CustomException :: {}", ex.getErrorCode());

        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResponse.toErrorResponse(errorCode));
    }

    private List<String> generateErrors(BindException ex) {
        List<String> errors = new ArrayList<>();
        List<ObjectError> all_errors = ex.getBindingResult().getAllErrors();

        for (ObjectError error : all_errors) {
            errors.add(error.getDefaultMessage());
        }
        return errors;
    }

}
