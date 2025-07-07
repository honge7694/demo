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
@DiscriminatorValue("FIXED")
public class FixedAmountDiscountCoupon extends Coupon {

    private Long discountAmount;

    protected FixedAmountDiscountCoupon(String name, int quantity, LocalDate issuedAt, LocalDate expiresAt, Long discountAmount) {
        super(name, quantity, issuedAt, expiresAt);
        this.discountAmount = discountAmount;
    }

    public static FixedAmountDiscountCoupon of(String name, int quantity, LocalDate issuedAt, LocalDate expiresAt, Long discountAmount) {
        return new FixedAmountDiscountCoupon(name, quantity, issuedAt, expiresAt, discountAmount);
    }

    @Override
    public Long calculateDiscountAmount(Long originalAmount) {
        return Math.min(originalAmount, this.discountAmount);
    }
}
