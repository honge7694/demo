package com.example.demo.common.config.response;

import lombok.Getter;

@Getter
public class ApiResult<T> {

    private String status;
    private String message;
    private T data;

    private ApiResult(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<T>(StatusCode.OK.getCode(), StatusCode.OK.getMessage(), data);
    }

    public static <T> ApiResult<T> error(String status, String message) {
        return new ApiResult<T>(status, message, null);
    }

}
