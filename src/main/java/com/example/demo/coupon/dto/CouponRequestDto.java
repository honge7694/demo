package com.example.demo.coupon.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CouponRequestDto {

    private String name;

    private Integer quantity;

    private LocalDate issuedAt;

    private LocalDate expiresAt;

    private DiscountType discountType;

    // 고정 할인 금액
    private Long discountAmount;

    // 비율 할인 금액
    private Integer discountRate;
    private Long maxDiscountAmount;

    public enum DiscountType {
        FIXED, PERCENTAGE
    }
}
