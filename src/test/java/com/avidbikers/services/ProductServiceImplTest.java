package com.avidbikers.services;

import com.avidbikers.data.dto.ProductDto;
import com.avidbikers.data.model.Product;
import com.avidbikers.data.repository.ProductCategoryRepository;
import com.avidbikers.data.repository.ProductRepository;
import com.avidbikers.web.exceptions.ProductException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductServiceImpl productService;


    private Product mockedProduct;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        mockedProduct = new Product();
        mockedProduct.setName("Iphone 12");
        mockedProduct.setId("001");
        mockedProduct.setDescription("Latest Iphone");
        mockedProduct.setCategoryId("001");
    }

    @Test
    @DisplayName("Vendor can add product")
    void addProduct(){
        when(productRepository.save(any(Product.class))).thenReturn(mockedProduct);
        when(modelMapper.map(new ProductDto(), Product.class)).thenReturn(mockedProduct);
        productService.addProduct(new ProductDto());
        verify(productRepository, times(1)).save(mockedProduct);

    }

    @Test
    @DisplayName("Vendor can remove product from dashboard")
    void removeProduct() throws ProductException {
        when(productRepository.findById(anyString())).thenReturn(Optional.of(mockedProduct));
        productService.removeProduct("001");
        verify(productRepository, times(1)).delete(mockedProduct);
    }

    @Test
    @DisplayName("Can product by id")
    void findById() throws ProductException {
        ProductDto productDto = new ProductDto();
        productDto.setName(mockedProduct.getName());
        productDto.setPrice(mockedProduct.getPrice());

        when(productRepository.findById(anyString())).thenReturn(Optional.of(mockedProduct));
        when(modelMapper.map(mockedProduct, ProductDto.class)).thenReturn(productDto);

        ProductDto product =  productService.findProductById("001");
        verify(productRepository, times(1)).findById("001");

        assertAll(
                ()-> assertThat(product.getName()).isEqualTo(mockedProduct.getName()),
                ()-> assertThat(product.getPrice()).isEqualTo(mockedProduct.getPrice())
        );
    }
}