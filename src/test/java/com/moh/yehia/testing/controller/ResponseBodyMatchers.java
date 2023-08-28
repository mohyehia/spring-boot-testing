package com.moh.yehia.testing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moh.yehia.testing.model.ValidationError;
import org.assertj.core.api.Assertions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Map;

public class ResponseBodyMatchers {
    public <T> ResultMatcher containsObjectAsJson(Object expected, Class<T> target) {
        return mvcResult -> {
            String jsonResponseAsString = mvcResult.getResponse().getContentAsString();
            T actualObject = new ObjectMapper().readValue(jsonResponseAsString, target);
            Assertions.assertThat(actualObject).usingRecursiveComparison().isEqualTo(expected);
        };
    }

    public ResultMatcher containsError(String expectedFieldName, String expectedMessage) {
        return mvcResult -> {
            String jsonResponseAsString = mvcResult.getResponse().getContentAsString();
            ValidationError validationError = new ObjectMapper().readValue(jsonResponseAsString, ValidationError.class);
            Map<String, String> errors = validationError.getErrors();
            String errorMessage = errors.getOrDefault(expectedFieldName, null);
            Assertions.assertThat(errorMessage)
                    .withFailMessage("expecting exactly 1 error message with field name {%s} and message {%s}", expectedFieldName, expectedMessage)
                    .isNotNull()
                    .isEqualTo(expectedMessage);
        };
    }

    static ResponseBodyMatchers responseBody() {
        return new ResponseBodyMatchers();
    }
}
