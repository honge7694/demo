package com.example.demo.point.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointChargeResponseDto {
    private Long userId;
    private String orderId;
    private int amount;
}
