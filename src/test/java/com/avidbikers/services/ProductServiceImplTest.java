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

import java.math.BigDecimal;
import java.util.List;
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
        mockedProduct.setName("iphone 12");
        mockedProduct.setId("001");
        mockedProduct.setDescription("Latest iphone");
        mockedProduct.setCategoryId("phone");
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

    @Test
    @DisplayName("Can view all product")
    void can_viewAllProductTest(){
        ProductDto productDto = new ProductDto();
        productDto.setName(mockedProduct.getName());
        productDto.setPrice(mockedProduct.getPrice());
        when(productRepository.findAll()).thenReturn(List.of(mockedProduct, mockedProduct));
        when(modelMapper.map(mockedProduct, ProductDto.class)).thenReturn(productDto);

        List<ProductDto> products = productService.getAllProducts();
        verify(productRepository, times(1)).findAll();

        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Product can be updated")
    void can_updateProductDetailTest() throws ProductException {

        //Given
        ProductDto productDto = new ProductDto();
        productDto.setPrice(BigDecimal.valueOf(2000));

        when(productRepository.findById(anyString())).thenReturn(Optional.of(mockedProduct));

        mockedProduct.setPrice(productDto.getPrice());
        when(productRepository.save(any(Product.class))).thenReturn(mockedProduct);

        productDto.setCategoryId(mockedProduct.getCategoryId());
        productDto.setDescription(mockedProduct.getDescription());
        productDto.setName(mockedProduct.getName());

        when(modelMapper.map(mockedProduct, ProductDto.class)).thenReturn(productDto);

        //When
        ProductDto updatedProduct = productService.updateProduct("001", productDto);

        //Assert
        verify(productRepository, times(1)).findById("001");
        verify(productRepository, times(1)).save(mockedProduct);
        verify(modelMapper, times(1)).map(mockedProduct, ProductDto.class);

        assertNotNull(updatedProduct);
        assertThat(BigDecimal.valueOf(2000)).isEqualTo(productDto.getPrice());
    }

    @Test
    @DisplayName("Can get all product under a specific category")
    void canGetAllProductUnderACategory(){
        String category = "phone";

        ProductDto productDto = new ProductDto();
        productDto.setCategoryId(mockedProduct.getCategoryId());
        productDto.setDescription(mockedProduct.getDescription());
        productDto.setName(mockedProduct.getName());

        when(productRepository.findProductsByCategoryId(anyString())).thenReturn(List.of(mockedProduct));
        when(modelMapper.map(mockedProduct, ProductDto.class)).thenReturn(productDto);

        List<ProductDto> products = productService.getAllProductsInCategory(category);

        verify(productRepository, times(1)).findProductsByCategoryId(category);
        verify(modelMapper, times(1)).map(mockedProduct, ProductDto.class);

        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Get all product containig product name")
    void canGetAllProductThatContainProductName(){
        String name = "iphone";

        ProductDto productDto = new ProductDto();
        productDto.setCategoryId(mockedProduct.getCategoryId());
        productDto.setDescription(mockedProduct.getDescription());
        productDto.setName(mockedProduct.getName());

        when(productRepository.findAllByName(anyString())).thenReturn(List.of(mockedProduct));
        when(modelMapper.map(mockedProduct, ProductDto.class)).thenReturn(productDto);

        List<ProductDto> products = productService.getAllProductContainingProductName(name);

        verify(productRepository, times(1)).findAllByName(name);
        verify(modelMapper, times(1)).map(mockedProduct, ProductDto.class);

        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Get all product that similar product description")
    void canGetAllProductThatHasSimilarDescription(){
        String description = "latest";

        ProductDto productDto = new ProductDto();
        productDto.setCategoryId(mockedProduct.getCategoryId());
        productDto.setDescription(mockedProduct.getDescription());
        productDto.setName(mockedProduct.getName());

        when(productRepository.findProductByDescriptionContaining(anyString())).thenReturn(List.of(mockedProduct));
        when(modelMapper.map(mockedProduct, ProductDto.class)).thenReturn(productDto);

        List<ProductDto> products = productService.getAllProductContainingProductDesc(description);

        verify(productRepository, times(1)).findProductByDescriptionContaining(description);
        verify(modelMapper, times(1)).map(mockedProduct, ProductDto.class);

        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(1);
    }

}