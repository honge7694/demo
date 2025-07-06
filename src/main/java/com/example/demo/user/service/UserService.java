package com.example.demo.user.service;

import com.example.demo.user.dto.UserRequestDto;
import com.example.demo.user.dto.UserResponseDto;
import com.example.demo.user.entity.User;
import com.example.demo.user.exception.UserErrorCode;
import com.example.demo.user.exception.UserException;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    /**
     * 유저 회원 가입
     * @param userRequestDto
     * @return
     */
    @Transactional
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        validateDuplicateEmail(userRequestDto.getEmail());

        User user = User.of(userRequestDto.getEmail(), userRequestDto.getPassword(), userRequestDto.getUsername());
        return UserResponseDto.from(userRepository.save(user));
    }

    private void validateDuplicateEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new UserException(UserErrorCode.DUPLICATED_USER_EMAIL);
        });
    }
}
