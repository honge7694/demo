package com.example.demo.point.entity;

import com.example.demo.point.dto.TransactionStatus;
import com.example.demo.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Long originalAmount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long discountAmount;

    private Long finalAmount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(unique = true)
    private String orderId;

    private String paymentKey;

    private LocalDateTime createdAt;

    protected PointHistory(User user, Long originalAmount, Long discountAmount, Long finalAmount, String orderId) {
        this.user = user;
        this.originalAmount = originalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.status = TransactionStatus.PENDING;
        this.orderId = orderId;
        this.createdAt = LocalDateTime.now();
    }

    public static PointHistory of(User user, Long originalAmount, Long discountAmount, Long finalAmount, String orderId) {
        return new PointHistory(user, originalAmount, discountAmount, finalAmount, orderId);
    }

    /* 비즈니스 로직 */
    public void updateOnSuccess(String paymentKey) {
        this.status = TransactionStatus.CHARGE;
        this.paymentKey = paymentKey;
    }

    public void updateOnFail() {
        this.status = TransactionStatus.FAIL;
    }
}
