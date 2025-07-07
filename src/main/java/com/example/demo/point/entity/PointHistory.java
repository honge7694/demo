package com.example.demo.point.entity;

import com.example.demo.point.dto.TransactionStatus;
import com.example.demo.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private long amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(unique = true)
    private String orderId;

    private String paymentKey;

    private LocalDateTime createdAt;

    public PointHistory(User user, int amount, String orderId) {
        this.user = user;
        this.amount = amount;
        this.status = TransactionStatus.PENDING;
        this.orderId = orderId;
        this.createdAt = LocalDateTime.now();
    }

    public static PointHistory of(User user, int amount, String orderId) {
        return new PointHistory(user, amount, orderId);
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
