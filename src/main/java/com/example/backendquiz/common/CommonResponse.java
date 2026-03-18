package com.example.backendquiz.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public CommonResponse(String message) {
        this.success = false;
        this.message = message;
        this.data = null;
    }

    public static <T> CommonResponse<T> successWithData(String message, T data) {
        return new CommonResponse<>(true, message, data);
    }

    public static <T> CommonResponse<T> fail(String message) {
        return new CommonResponse<>(message);
    }

}
