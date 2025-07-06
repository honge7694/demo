package com.example.demo.user.service;

import com.example.demo.user.dto.UserRequestDto;
import com.example.demo.user.dto.UserResponseDto;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.UserViewedEvent;
import com.example.demo.user.exception.UserErrorCode;
import com.example.demo.user.exception.UserException;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

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

    /**
     * 유저 목록 조회
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        List<UserResponseDto> userDtoList = userPage.getContent().stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
        return new PageImpl<>(userDtoList, pageable, userPage.getTotalElements());
    }

    /**
     * 유저 상세 조회, viewCount + 1
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_USER_ID));

        // 이벤트에게 조회수 증가 위임
        eventPublisher.publishEvent(new UserViewedEvent(userId));

        return UserResponseDto.from(user);
    }

}
