package com.moh.yehia.testing.asserts;

import com.moh.yehia.testing.model.Category;
import com.moh.yehia.testing.model.Product;

public class ProjectAssertions {
    public static ProductAssert assertThat(Product actual) {
        return new ProductAssert(actual);
    }

    public static CategoryAssert assertThat(Category actual) {
        return new CategoryAssert(actual);
    }
}
