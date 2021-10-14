package com.avidbikers.web.payload;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse {
    private boolean isSuccessful;
    private String message;
    private LocalDateTime timeStamp;

    public ApiResponse(boolean isSuccessful, String message) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        timeStamp = LocalDateTime.now();
    }
}
