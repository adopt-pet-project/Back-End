package com.adoptpet.server.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    MISSING_REGISTER_BAD_REQUEST(BAD_REQUEST, "등록자에 대한 정보가 입력되지 않았습니다."),
    MISSING_FILE_BAD_REQUEST(BAD_REQUEST,"요청에 대한 이미지 파일이 누락되었습니다."),


    /* 403 FORBIDDEN : 접근 권한 제한 */
    /* Valid : 유효한 */
    VALID_USER_ID(FORBIDDEN, "해당 정보에 접근 권한이 존재하지 않습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(NOT_FOUND, "해당 이메일을 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(NOT_FOUND,"해당 이미지를 찾을 수 없습니다."),
    IMAGE_TYPE_NOT_FOUND(NOT_FOUND,"해당 이미지 타입을 찾을 수 없습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    /* DUPLICATE : (다른 무엇과) 똑같은 */
    DUPLICATE_EMAIL(CONFLICT, "이메일이 이미 존재합니다."),

    /* 500 : */
    UNSUCCESSFUL_UPLOAD(INTERNAL_SERVER_ERROR,"이미지 파일 업로드에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String massage;

    public static CustomException throwNotEnterEmailOrToken(){
        throw new CustomException(MISSING_REGISTER_BAD_REQUEST);
    }

    public static CustomException throwMissingFileRequestPart() {
        throw new CustomException(MISSING_FILE_BAD_REQUEST);
    }

    public static CustomException throwImageNotFound() {
        throw new CustomException(IMAGE_NOT_FOUND);
    }

    public static CustomException throwImageTypeNotFound() {
        throw new CustomException(IMAGE_TYPE_NOT_FOUND);
    }
}