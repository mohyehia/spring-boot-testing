package com.moh.yehia.testing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.springframework.test.web.servlet.ResultMatcher;

public class ResponseBodyMatchers {
    public <T> ResultMatcher containsObjectAsJson(Object expected, Class<T> target) {
        return mvcResult -> {
            String jsonResponseAsString = mvcResult.getResponse().getContentAsString();
            T actualObject = new ObjectMapper().readValue(jsonResponseAsString, target);
            Assertions.assertThat(actualObject).usingRecursiveComparison().isEqualTo(expected);
        };
    }

    static ResponseBodyMatchers responseBody() {
        return new ResponseBodyMatchers();
    }
}
