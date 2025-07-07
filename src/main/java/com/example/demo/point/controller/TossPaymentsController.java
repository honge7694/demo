package com.example.demo.point.controller;

import com.example.demo.common.config.response.ApiResult;
import com.example.demo.point.dto.PointChargeRequestDto;
import com.example.demo.point.dto.PointChargeResponseDto;
import com.example.demo.point.service.TossPaymentsService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class TossPaymentsController {

    private final TossPaymentsService tossPaymentsService;

    @PostMapping("/charge/request")
    public ResponseEntity<ApiResult<PointChargeResponseDto>> requestCharge(@RequestBody PointChargeRequestDto requestDto) {
        return ResponseEntity.ok(ApiResult.success(tossPaymentsService.requestCharge(requestDto)));
    }

    @PostMapping("/confirm")
    public ResponseEntity<ApiResult<JSONObject>> confirmPayment(
            @RequestBody String jsonBody
    ) throws IOException, ParseException {
        return ResponseEntity.ok(ApiResult.success(tossPaymentsService.confirmPayment(jsonBody)));
    }
}
