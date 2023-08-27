package com.moh.yehia.testing.asserts;

import com.moh.yehia.testing.model.Category;
import org.assertj.core.api.AbstractAssert;

public class CategoryAssert extends AbstractAssert<CategoryAssert, Category> {
    protected CategoryAssert(Category actual) {
        super(actual, CategoryAssert.class);
    }

    public static CategoryAssert assertThat(Category category){
        return new CategoryAssert(category);
    }

    public CategoryAssert hasId(){
        isNotNull();
        if(actual.getId() == null){
            failWithMessage("Expected category to have an id, but it was null");
        }
        return this;
    }

    public CategoryAssert hasName(String name){
        isNotNull();
        if(!actual.getName().equals(name)){
            failWithMessage("Expected category name to be {%s} but it was {%s}", name, actual.getName());
        }
        return this;
    }

    public CategoryAssert hasDescription(String description){
        isNotNull();
        if (!actual.getDescription().equals(description)){
            failWithMessage("Expected category description to be {%s} but it was {%s}", description, actual.getDescription());
        }
        return this;
    }

}
