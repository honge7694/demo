package com.example.demo.point.dto;

import lombok.Data;

@Data
public class PointChargeRequestDto {
    private Long userId;
    private Long amount;
    private Long couponId;
}
