package com.avidbikers.web.controllers;

import com.avidbikers.data.dto.ProductDto;
import com.avidbikers.data.repository.ProductRepository;
import com.avidbikers.data.repository.UserRepository;
import com.avidbikers.services.ProductServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private String productJson;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    ProductServiceImpl productService;
    ProductDto mockedProduct;


    @BeforeEach
    void setUp() throws JsonProcessingException {
        mockedProduct = new ProductDto();
        mockedProduct.setName("iphone 12");
        mockedProduct.setDescription("Latest iphone");
        mockedProduct.setCategoryId("phone");
        productJson = objectMapper.writeValueAsString(mockedProduct);
    }

    @AfterEach
    void tearDown() {
        mockedProduct = null;
        productJson = null;
    }

    @Test
    void whenProductIsCreatedWithValidDetails_return201() throws Exception{
        mockMvc.perform(post("/ap1/v1/avidbikers/product/new")
                .contentType("application/json")
                .content(productJson)).andDo(print())
                .andExpect(status().isCreated());
    }
}