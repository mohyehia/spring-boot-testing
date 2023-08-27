package com.moh.yehia.testing.asserts;

import com.moh.yehia.testing.model.ApiError;
import org.assertj.core.api.AbstractAssert;

public class ApiErrorAssert extends AbstractAssert<ApiErrorAssert, ApiError> {
    protected ApiErrorAssert(ApiError actual) {
        super(actual, ApiErrorAssert.class);
    }

    public static ApiErrorAssert assertThat(ApiError apiError) {
        return new ApiErrorAssert(apiError);
    }

    public ApiErrorAssert hasStatusCode(String statusCode) {
        isNotNull();
        if (!actual.getStatusCode().equals(statusCode)) {
            failWithMessage("Expected apiError statusCode to be {%s} but it was {%s}", statusCode, actual.getStatusCode());
        }
        return this;
    }

    public ApiErrorAssert hasMessage(String message) {
        isNotNull();
        if (!actual.getMessage().equals(message)) {
            failWithMessage("Expected apiError message to be {%s} but it was {%s}", message, actual.getMessage());
        }
        return this;
    }

    public ApiErrorAssert hasPath(String path) {
        isNotNull();
        if (!actual.getPath().equals(path)) {
            failWithMessage("Expected apiError path to be {%s} but it was {%s}", path, actual.getPath());
        }
        return this;
    }
}
