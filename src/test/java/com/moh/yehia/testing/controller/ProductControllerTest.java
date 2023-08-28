package com.moh.yehia.testing.controller;

import com.github.javafaker.Faker;
import com.moh.yehia.testing.asserts.ApiErrorAssert;
import com.moh.yehia.testing.asserts.ProductAssert;
import com.moh.yehia.testing.model.ApiError;
import com.moh.yehia.testing.model.Product;
import com.moh.yehia.testing.model.ProductRequest;
import com.moh.yehia.testing.service.design.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

class ProductControllerTest extends GlobalSpringContext {

    private static Faker faker;

    @MockBean
    private ProductService productService;

    private final String API_URL = "/api/v1/products";

    @BeforeAll
    static void initializeFaker() {
        faker = new Faker(Locale.ENGLISH);
    }

    @Test
    void shouldReturnAllProducts() throws Exception {
        // mock
        List<Product> expectedProducts = populateRandomProducts();
        // given
        BDDMockito.given(productService.findAll()).willReturn(expectedProducts);
        // when or assertions or perform mocks
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String actualResponseAsString = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualResponseAsString).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedProducts)
        );
    }

    @Test
    void shouldReturnProductWhenValidId() throws Exception {
        // mock
        Product product = populateRandomProduct();
        // given
        BDDMockito.given(productService.findById(ArgumentMatchers.anyString())).willReturn(product);
        // assertion
        mockMvc.perform(
                        MockMvcRequestBuilders.get(API_URL + "/{id}", "123456")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(ResponseBodyMatchers.responseBody().containsObjectAsJson(product, Product.class));
    }

    @Test
    void shouldThrowAnExceptionWhenInvalidProductId() throws Exception {
        // given
        BDDMockito.given(productService.findById(ArgumentMatchers.anyString())).willReturn(null);
        // assertion
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(API_URL + "/{id}", "123456")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        String actualResponseAsString = mvcResult.getResponse().getContentAsString();
        ApiError actualApiError = objectMapper.readValue(actualResponseAsString, ApiError.class);
        ApiErrorAssert.assertThat(actualApiError)
                .hasStatusCode("INVALID_REQUEST")
                .hasMessage("Product not found with this id: 123456")
                .hasPath("uri=" + API_URL + "/123456");
    }

    @Test
    void shouldSaveProductWhenValidData() throws Exception {
        // mock
        ProductRequest productRequest = populateProductRequest();
        Product expectedProduct = populateProductFromProductRequest(productRequest);
        // given
        BDDMockito.given(productService.save(ArgumentMatchers.any(ProductRequest.class))).willReturn(expectedProduct);
        // assert
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post(API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productRequest))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        String actualResponseAsString = mvcResult.getResponse().getContentAsString();
        Product actualProduct = objectMapper.readValue(actualResponseAsString, Product.class);
        ProductAssert.assertThat(actualProduct)
                .hasId()
                .hasName(expectedProduct.getName())
                .hasDescription(expectedProduct.getDescription())
                .hasPrice(expectedProduct.getPrice())
                .hasCategoryId(expectedProduct.getCategoryId())
                .hasStock(expectedProduct.getStock());
    }

    @Test
    void shouldThrowAnExceptionWhenInvalidProductRequest() throws Exception {
        // mock
        ProductRequest productRequest = new ProductRequest("", "", null, "", 0);
        // assert
        mockMvc.perform(
                        MockMvcRequestBuilders.post(API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productRequest))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(ResponseBodyMatchers.responseBody().containsError("name", "must not be blank"))
                .andExpect(ResponseBodyMatchers.responseBody().containsError("description", "must not be blank"))
                .andExpect(ResponseBodyMatchers.responseBody().containsError("price", "must not be null"))
                .andExpect(ResponseBodyMatchers.responseBody().containsError("categoryId", "must not be blank"))
                .andExpect(ResponseBodyMatchers.responseBody().containsError("stock", "must be greater than or equal to 1"));
    }


    private List<Product> populateRandomProducts() {
        return Arrays.asList(
                Product.builder().id(UUID.randomUUID().toString()).name(faker.commerce().productName()).description(faker.funnyName().name()).price(new BigDecimal(faker.commerce().price())).categoryId(UUID.randomUUID().toString()).build(),
                Product.builder().id(UUID.randomUUID().toString()).name(faker.commerce().productName()).description(faker.funnyName().name()).price(new BigDecimal(faker.commerce().price())).categoryId(UUID.randomUUID().toString()).build(),
                Product.builder().id(UUID.randomUUID().toString()).name(faker.commerce().productName()).description(faker.funnyName().name()).price(new BigDecimal(faker.commerce().price())).categoryId(UUID.randomUUID().toString()).build()
        );
    }

    private Product populateRandomProduct() {
        return Product.builder()
                .id(UUID.randomUUID().toString())
                .id(UUID.randomUUID().toString())
                .name(faker.commerce().productName())
                .description(faker.funnyName().name())
                .price(new BigDecimal(faker.commerce().price()))
                .stock(faker.number().numberBetween(1, 100))
                .categoryId(UUID.randomUUID().toString())
                .build();
    }

    private ProductRequest populateProductRequest() {
        return new ProductRequest(faker.commerce().productName(), faker.funnyName().name(), new BigDecimal(faker.commerce().price()), faker.commerce().department(), faker.number().numberBetween(1, 100));
    }

    private Product populateProductFromProductRequest(ProductRequest productRequest) {
        return Product.builder()
                .id(UUID.randomUUID().toString())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .categoryId(productRequest.getCategoryId())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .build();
    }
}