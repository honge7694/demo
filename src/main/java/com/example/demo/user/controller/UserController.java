package com.example.demo.user.controller;

import com.example.demo.common.config.response.ApiResult;
import com.example.demo.user.dto.UserRequestDto;
import com.example.demo.user.dto.UserResponseDto;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/v1/create")
    public ResponseEntity<ApiResult<UserResponseDto>> registerUser(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(ApiResult.success(userService.registerUser(userRequestDto)));
    }
}
