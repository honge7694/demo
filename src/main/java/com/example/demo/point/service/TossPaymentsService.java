package com.example.demo.point.service;

import com.example.demo.coupon.entity.Coupon;
import com.example.demo.coupon.entity.FixedAmountDiscountCoupon;
import com.example.demo.coupon.entity.PercentageDiscountCoupon;
import com.example.demo.coupon.exception.CouponErrorCode;
import com.example.demo.coupon.exception.CouponException;
import com.example.demo.coupon.repository.CouponRepository;
import com.example.demo.point.PointHistoryRepository;
import com.example.demo.point.dto.PointChargeRequestDto;
import com.example.demo.point.dto.PointChargeResponseDto;
import com.example.demo.point.entity.PointHistory;
import com.example.demo.point.exception.PointErrorCode;
import com.example.demo.point.exception.PointException;
import com.example.demo.user.entity.User;
import com.example.demo.user.exception.UserErrorCode;
import com.example.demo.user.exception.UserException;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentsService {

    @Value("${toss.payments.secretKey}")
    private String widgetSecretKey;

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public PointChargeResponseDto requestCharge(PointChargeRequestDto requestDto) {
        User user = findByUserId(requestDto.getUserId());

        // 고유한 orderID 생성
        String orderId = UUID.randomUUID().toString();

        // 쿠폰 금액 계산
        Long originalAmount = requestDto.getAmount();
        Long discountAmount = 0L;

        if (requestDto.getCouponId() != null) {
            Coupon coupon = couponRepository.findById(requestDto.getCouponId())
                    .orElseThrow(() -> new CouponException(CouponErrorCode.COUPON_NOT_FOUND));
            if (coupon instanceof FixedAmountDiscountCoupon) {
                discountAmount = coupon.calculateDiscountAmount(originalAmount);
            } else if (coupon instanceof PercentageDiscountCoupon) {
                discountAmount = coupon.calculateDiscountAmount(originalAmount);
            }
        }

        // 최종 금액 계산
        Long finalAmount = originalAmount - discountAmount;
        if (finalAmount < 0) {
            finalAmount = 0L;
        }

        // pointHistory 생성
        PointHistory pointHistory = PointHistory.of(user, originalAmount, discountAmount, finalAmount, orderId);
        pointHistoryRepository.save(pointHistory);
        return new PointChargeResponseDto(user.getId(), orderId, originalAmount, discountAmount, finalAmount);
    }

    @Transactional
    public JSONObject confirmPayment(String jsonBody) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        String orderId;
        Long userId;
        Long amount;
        String paymentKey;
        try {
            // 클라이언트에서 받은 JSON 요청 바디입니다.
            JSONObject requestData = (JSONObject) parser.parse(jsonBody);
            paymentKey = (String) requestData.get("paymentKey");
            userId = (Long) requestData.get("userId");
            orderId = (String) requestData.get("orderId");
            amount = ((Long) requestData.get("amount"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);
        obj.put("paymentKey", paymentKey);

        // 결제 요청에서 생성한 orderId가 있는지 확인
        PointHistory pointHistory = pointHistoryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PointException(PointErrorCode.NOT_FOUND_POINT_HISTORY));

        // 요청 금액과 실제 금액이 일치하는지 확인
        if (!pointHistory.getFinalAmount().equals(amount)) {
            log.error("요청 금액 : {}, 실제 금액: {}", pointHistory.getOriginalAmount(), amount);
            throw new PointException(PointErrorCode.BAD_REQUEST_POINT);
        }

        // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        // 결제 성공 및 실패 비즈니스 로직을 구현하세요.
        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();

        if (isSuccess) {
            log.info("결제 성공, 주문 ID: {}, 금액: {}", orderId, pointHistory.getFinalAmount());
            User user = findByUserId(userId);
            user.addPoint(pointHistory.getFinalAmount());

            // 결제 상태 변경
            pointHistory.updateOnSuccess(paymentKey);
        } else {
            String errorCode = (String) jsonObject.get("code");
            String errorMsg = (String) jsonObject.get("message");
            log.error("결제 실패, 주문 ID: {}, 에러 코드: {}, 에러 메시지: {}", orderId, errorCode, errorMsg);
            pointHistory.updateOnFail();
            switch(errorCode) {
                case "NOT_FOUND_PAYMENT_SESSION":
                    throw new PointException(PointErrorCode.NOT_FOUND_PAYMENT_SESSION);
                default:
                    throw new PointException(PointErrorCode.PAYMENT_GATEWAY_UNAVAILABLE);
            }
        }

        return jsonObject;
    }

    private User findByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_USER_ID));
        return user;
    }
}
