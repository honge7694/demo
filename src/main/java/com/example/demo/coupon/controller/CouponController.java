package com.example.demo.coupon.controller;

import com.example.demo.common.config.response.ApiResult;
import com.example.demo.coupon.dto.CouponRequestDto;
import com.example.demo.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons/")
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/v1/create")
    public ResponseEntity<ApiResult<Long>> couponCreate(@RequestBody CouponRequestDto couponRequestDto) {
        return ResponseEntity.ok(ApiResult.success(couponService.couponCreate(couponRequestDto)));
    }
}
