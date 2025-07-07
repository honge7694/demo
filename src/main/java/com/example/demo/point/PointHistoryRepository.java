package com.example.demo.point;

import com.example.demo.point.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Integer> {

    Optional<PointHistory> findByOrderId(String orderId);
}
