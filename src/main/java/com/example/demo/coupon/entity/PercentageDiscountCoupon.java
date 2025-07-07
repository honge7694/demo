package com.example.demo.coupon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("PERCENTAGE")
public class PercentageDiscountCoupon extends Coupon {

    private int discountRate;

    private Long maxDiscountAmount;

    protected PercentageDiscountCoupon(String name, int quantity, LocalDate issuedAt, LocalDate expiresAt, int discountRate, Long maxDiscountAmount) {
        super(name, quantity, issuedAt, expiresAt);
        this.discountRate = discountRate;
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public static PercentageDiscountCoupon of(String name, int quantity, LocalDate issuedAt, LocalDate expiresAt, int discountRate, Long maxDiscountAmount) {
        return new PercentageDiscountCoupon(name, quantity, issuedAt, expiresAt, discountRate, maxDiscountAmount);
    }

    /* 비즈니스 로직 */
    @Override
    public Long calculateDiscountAmount(Long originalAmount) {
        long discount = originalAmount * discountRate / 100;

        // 최대 할인 금액이 적용되어 있고, 계산된 할인액이 더 크면 최대 할인 금액 적용
        if (maxDiscountAmount != null && discount > maxDiscountAmount) {
            return maxDiscountAmount;
        }
        return discount;
    }
}
