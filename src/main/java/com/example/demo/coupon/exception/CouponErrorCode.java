package com.example.demo.coupon.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CouponErrorCode {

    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "404000", "존재하지 않는 쿠폰입니다."),
    COUPON_CONFLICT(HttpStatus.CONFLICT, "409000", "쿠폰의 개수가 부족합니다."),
    FIXED_COUPON_BAD_REQUEST(HttpStatus.BAD_REQUEST, "400000", "정액 할인 쿠폰 금액을 입력해주세요. 또는 쿠폰 타입을 입력해주세요."),
    PERCENTAGE_COUPON_BAD_REQUEST(HttpStatus.BAD_REQUEST, "400001", "비율 할인 쿠폰 비율을 입력해주세요. 또는 쿠폰 타입을 입력해주세요.");

    private final HttpStatus httpstatus;
    private final String code;
    private final String message;

    CouponErrorCode(HttpStatus httpstatus, String code, String message) {
        this.httpstatus = httpstatus;
        this.code = code;
        this.message = message;
    }
}
