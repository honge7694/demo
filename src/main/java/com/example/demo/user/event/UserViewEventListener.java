package com.example.demo.user.event;

import com.example.demo.user.entity.UserViewedEvent;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserViewEventListener {

    private final UserRepository userRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserViewCountEvent(UserViewedEvent event) {
        log.info("유저 ViewCount 증가를 시작합니다.");

        try {
            userRepository.incrementViewCount(event.getUserId());
        } catch (Exception e) {
            log.error("유저 ViewCount 업데이트 과정에서 에러가 발생했습니다. userId: {}, errorMessage: {}", event.getUserId(), e.getMessage());
        }
    }
}
