package com.example.siderswebapp.exception;

import lombok.Getter;

@Getter
enum ErrorUtils {

    POST_NOT_FOUND(
            404,
            "존재하지 않는 글입니다.",
            "POST-ERR-404"
    ),
    MEMBER_NOT_FOUND(
            404,
            "존재하지 않는 회원입니다.",
            "MEMBER-ERR-404"
    ),
    IS_NOT_OWNER(
            403,
            "해당 글에 대한 권한이 없습니다.",
            "MEMBER-ERR-403"
    ),
    JWT_NOT_AVAILABLE(
            403,
            "토큰이 유효하지 않습니다.",
            "JWT-ERR-403"
    ),
    IS_NOT_SUPPORTED_OAUTH2_LOGIN(
            404,
            "지원하지 않는 로그인 방식입니다.",
            "AUTHORIZATION-ERR-404"
    )
    ;

    private final int status;
    private final String message;
    private final String errorCode;

    ErrorUtils(int status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}
