package com.example.demo.coupon.service;

import com.example.demo.coupon.dto.CouponRequestDto;
import com.example.demo.coupon.entity.Coupon;
import com.example.demo.coupon.entity.FixedAmountDiscountCoupon;
import com.example.demo.coupon.entity.PercentageDiscountCoupon;
import com.example.demo.coupon.exception.CouponErrorCode;
import com.example.demo.coupon.exception.CouponException;
import com.example.demo.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional
    public Long couponCreate(CouponRequestDto couponRequestDto) {
        Coupon coupon = switch (couponRequestDto.getDiscountType()) {
            case FIXED -> createFixedAmountDiscountCoupon(couponRequestDto);
            case PERCENTAGE -> createPercentageDiscountCoupon(couponRequestDto);
        };
        coupon = couponRepository.save(coupon);
        return coupon.getId();
    }

    private FixedAmountDiscountCoupon createFixedAmountDiscountCoupon(CouponRequestDto requestDto) {
        if (requestDto.getDiscountType() == null || requestDto.getDiscountAmount() <= 0) {
            throw new CouponException(CouponErrorCode.FIXED_COUPON_BAD_REQUEST);
        }

        return FixedAmountDiscountCoupon.of(
                requestDto.getName(),
                requestDto.getQuantity(),
                requestDto.getIssuedAt(),
                requestDto.getExpiresAt(),
                requestDto.getDiscountAmount()
        );
    }

    private PercentageDiscountCoupon createPercentageDiscountCoupon(CouponRequestDto requestDto) {
        if (requestDto.getDiscountType() == null || requestDto.getDiscountRate() <= 0) {
            throw new CouponException(CouponErrorCode.PERCENTAGE_COUPON_BAD_REQUEST);
        }

        return PercentageDiscountCoupon.of(
                requestDto.getName(),
                requestDto.getQuantity(),
                requestDto.getIssuedAt(),
                requestDto.getExpiresAt(),
                requestDto.getDiscountRate(),
                requestDto.getMaxDiscountAmount()
        );
    }
}
