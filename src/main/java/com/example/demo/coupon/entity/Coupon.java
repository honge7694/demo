package com.example.demo.coupon.entity;

import com.example.demo.coupon.exception.CouponErrorCode;
import com.example.demo.coupon.exception.CouponException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "coupon_type")
public abstract class Coupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private int quantity;

    @Column(nullable = false)
    private LocalDate issuedAt;

    @Column(nullable = false)
    private LocalDate expiresAt;

    protected Coupon(String name, int quantity, LocalDate issuedAt, LocalDate expiresAt) {
        this.name = name;
        this.quantity = quantity;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    /* 비즈니스 로직 */

    /**
     * 쿠폰 재고 감소
     */
    public void reduceCoupon() {
        if (quantity < 0) {
            throw new CouponException(CouponErrorCode.COUPON_CONFLICT);
        }
        quantity -= 1;
    }

    /**
     * 할인 금액 계산하는 추상메서드
     */
    public abstract Long calculateDiscountAmount(Long originalAmount);
}

