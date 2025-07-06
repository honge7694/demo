package com.example.demo.user.dto;

import com.example.demo.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String username;
    private int viewCount;
    private int balance;
    private LocalDateTime createdAt;

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getViewCount(),
                user.getBalance(),
                user.getCreatedAt()
        );
    }
}
