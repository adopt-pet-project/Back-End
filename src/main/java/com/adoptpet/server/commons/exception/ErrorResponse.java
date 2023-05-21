package com.adoptpet.server.commons.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@EqualsAndHashCode
public class ErrorResponse {
    private final Integer status;
    private final String message;
    private final List<String> errors;

    public ErrorResponse(Integer status, String message, String error) {
        this.status = status;
        this.message = message;
        this.errors = List.of(error);
    }

    public ErrorResponse(Integer status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public static ErrorResponse toErrorResponse(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.getStatus(),
                errorCode.name(),
                errorCode.getMassage());
    }
}
