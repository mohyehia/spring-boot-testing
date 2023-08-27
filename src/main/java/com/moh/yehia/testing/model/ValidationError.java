package com.moh.yehia.testing.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ValidationError {
    private String statusCode;
    private String message;
    private String path;
    private Map<String, String> errors;

    public ValidationError() {
        statusCode = "INVALID_REQUEST";
        errors = new HashMap<>();
    }

    public ValidationError(String path, String message) {
        this();
        this.path = path;
        this.message = message;
    }

    public void addError(String errorCode, String errorMessage) {
        errors.put(errorCode, errorMessage);
    }
}
