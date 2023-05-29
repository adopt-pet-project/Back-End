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
    DELETED_ARTICLE_BAD_REQUEST(BAD_REQUEST,"해당 게시글이 삭제된 상태입니다."),

    /* 403 FORBIDDEN : 접근 권한 제한 */
    /* Valid : 유효한 */
    VALID_USER_ID(FORBIDDEN, "해당 정보에 접근 권한이 존재하지 않습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(NOT_FOUND, "해당 이메일을 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(NOT_FOUND,"해당 이미지를 찾을 수 없습니다."),
    TYPE_NOT_FOUND(NOT_FOUND,"해당 타입을 찾을 수 없습니다."),
    ARTICLE_NOT_FOUND(NOT_FOUND,"해당 게시글을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(NOT_FOUND,"해당 카테고리를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND,"해당 댓글을 찾을 수 없습니다."),
    NOTIFICATION_NOT_FOUND(NOT_FOUND, "해당 알림을 찾을 수 없습니다." ),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    /* DUPLICATE : (다른 무엇과) 똑같은 */
    DUPLICATE_EMAIL(CONFLICT, "이메일이 이미 존재합니다."),
    DUPLICATE_DIFFERENT_USER(CONFLICT, "해당 글의 작성자와 정보가 일치하지 않습니다."),
    DUPLICATE_BOOKMARK(CONFLICT, "해당 게시글은 이미 관심글로 등록되어 있습니다."),
    DUPLICATE_NOT_ADDITION_BOOKMARK(CONFLICT, "해당 글은 관심글로 등록되어 있지 않습니다."),
    DUPLICATE_HEART(CONFLICT, "해당 글을 좋아요한 상태입니다."),
    DUPLICATE_NOT_HEART(CONFLICT, "해당 글을 좋아요 하지 않은 상태입니다."),


    /* 500 : */
    UNSUCCESSFUL_UPLOAD(INTERNAL_SERVER_ERROR,"이미지 파일 업로드에 실패했습니다."),
    UNSUCCESSFUL_INSERT(INTERNAL_SERVER_ERROR,"해당 정보 저장이 실패했습니다."),
    UNSUCCESSFUL_MODIFY(INTERNAL_SERVER_ERROR,"해당 정보의 상태 변경에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String detail;

    public static CustomException throwNotEnterEmailOrToken(){
        throw new CustomException(MISSING_REGISTER_BAD_REQUEST);
    }

    public static CustomException throwMissingFileRequestPart() {
        throw new CustomException(MISSING_FILE_BAD_REQUEST);
    }

    public static CustomException throwImageNotFound() {
        throw new CustomException(IMAGE_NOT_FOUND);
    }

    public static CustomException throwTypeNotFound() {
        throw new CustomException(TYPE_NOT_FOUND);
    }

    public static CustomException throwCommentNotFound(){
        throw new CustomException(COMMENT_NOT_FOUND);
    }

    public static CustomException throwEmailNotFound(){
        throw new CustomException(EMAIL_NOT_FOUND);
    }

    public static CustomException throwArticleNotFound() {
        throw new CustomException(ARTICLE_NOT_FOUND);
    }
    public static CustomException throwDuplicateBookmark(){throw new CustomException(DUPLICATE_BOOKMARK);}

    public static CustomException throwNotificationNotFound() {
        throw new CustomException(NOTIFICATION_NOT_FOUND);
    }
}