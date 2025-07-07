package com.example.demo.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(name = "view_count")
    private int viewCount;

    private int balance;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    protected User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.viewCount = 0;
        this.balance = 0;
    }

    public static User of(String email, String password, String name) {
        return new User(email, password, name);
    }

    /* 비즈니스 로직*/
    public void addPoint(int amount) {
        balance += amount;
    }
}
