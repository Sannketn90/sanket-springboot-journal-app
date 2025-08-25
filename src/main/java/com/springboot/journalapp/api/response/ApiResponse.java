package com.springboot.journalapp.api.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

    private String status;
    private T data;
    private String message;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status("success")
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .status("error")
                .data(null)
                .message(message)
                .build();
    }

}
