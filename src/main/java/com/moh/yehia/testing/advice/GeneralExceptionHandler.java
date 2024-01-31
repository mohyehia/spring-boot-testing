package com.moh.yehia.testing.advice;

import com.moh.yehia.testing.exception.InvalidRequestException;
import com.moh.yehia.testing.model.ApiError;
import com.moh.yehia.testing.model.ValidationError;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
@Log4j2
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiError> handleInvalidRequest(InvalidRequestException e, WebRequest webRequest) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(
                new ApiError("INVALID_REQUEST", e.getMessage(), webRequest.getDescription(false)),
                HttpStatus.BAD_REQUEST
        );
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode httpStatusCode, WebRequest request) {
        ValidationError validationError = new ValidationError(request.getDescription(false), "Invalid Request Data, Your request is either missing required data or contains invalid values");
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        fieldErrors.forEach(fieldError -> validationError.addError(fieldError.getField(), fieldError.getDefaultMessage()));
        globalErrors.forEach(globalError -> validationError.addError(globalError.getObjectName(), globalError.getDefaultMessage()));
        return new ResponseEntity<>(validationError, HttpStatus.BAD_REQUEST);
    }
}
