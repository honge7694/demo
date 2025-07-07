package com.example.demo.common.exception;

import com.example.demo.common.config.response.ApiResult;
import com.example.demo.point.exception.PointException;
import com.example.demo.user.exception.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UserException.class)
    public ResponseEntity<ApiResult<Void>> handleUserException(UserException e) {
        return ResponseEntity
                .status(e.getUserErrorCode().getHttpStatus())
                .body(ApiResult.error(e.getUserErrorCode().getCode(), e.getMessage()));
    }

    @ExceptionHandler(value = PointException.class)
    public ResponseEntity<ApiResult<Void>> handlerPointException(PointException e) {
        return ResponseEntity
                .status(e.getPointErrorCode().getHttpStatus())
                .body(ApiResult.error(e.getPointErrorCode().getCode(), e.getMessage()));
    }
}
