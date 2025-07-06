package com.example.demo.user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode {

    DUPLICATED_USER_EMAIL(HttpStatus.CONFLICT, "409000", "이미 가입된 사용자 이메일입니다."),
    NOT_FOUND_USER_ID(HttpStatus.NOT_FOUND, "404000", "가입된 유저를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    UserErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
