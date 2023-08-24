package com.moh.yehia.testing.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ApiError {
    private String statusCode;
    private String message;
    private String path;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy HH:mm:ss")
    private LocalDateTime timestamp;

    public ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(String statusCode, String message, String path) {
        this();
        this.statusCode = statusCode;
        this.message = message;
        this.path = path;
    }
}
