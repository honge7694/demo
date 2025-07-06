package com.example.demo.common.config.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StatusCode {

    OK(HttpStatus.OK, "200000", "성공"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "400000", "잘못된 요청입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}