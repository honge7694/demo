package com.example.demo.point.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PointErrorCode {

    NOT_FOUND_POINT_HISTORY(HttpStatus.NOT_FOUND, "404000", "존재하지 않는 주문 내역입니다."),
    BAD_REQUEST_POINT(HttpStatus.BAD_REQUEST, "400000", "요청 금액과 실제 금액이 일치하지 않습니다."),
    PAYMENT_GATEWAY_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "503000", "결제 서비스와의 통신에 실패했습니다. 잠시 후 다시 시도해주세요."),
    NOT_FOUND_PAYMENT_SESSION(HttpStatus.NOT_FOUND, "404001", "결제 시간이 만료되어 결제 진행 데이터가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    PointErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
