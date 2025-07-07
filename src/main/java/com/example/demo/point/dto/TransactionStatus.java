package com.example.demo.point.dto;

public enum TransactionStatus {
    PENDING("PENDING", "유저 포인트 충전 대기중"),
    CHARGE("CHARGE", "유저 포인트 충전"),
    FAIL("FAIL", "유저 포인트 충전 실패");

    private final String type;
    private final String description;

    TransactionStatus(String type, String description) {
        this.type = type;
        this.description = description;
    }
}
