package com.moh.yehia.testing.advice;

import com.moh.yehia.testing.exception.InvalidRequestException;
import com.moh.yehia.testing.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiError> handleInvalidRequest(InvalidRequestException e, WebRequest webRequest) {
        e.printStackTrace();
        return new ResponseEntity<>(
                new ApiError("INVALID_REQUEST", e.getMessage(), webRequest.getDescription(false)),
                HttpStatus.BAD_REQUEST
        );
    }
}
