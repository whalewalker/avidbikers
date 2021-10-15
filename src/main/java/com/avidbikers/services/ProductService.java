package com.avidbikers.services;

import com.avidbikers.data.dto.ProductDto;

import java.util.List;

public interface ProductService {
    void addProduct(ProductDto productDto);

    void removeProduct(String productId);

    void removeProduct(String productId, int quantity);

    ProductDto findProductById(String productId);

    List<ProductDto> getAllProducts();

    ProductDto updateProduct(String productId, ProductDto updatedProductDetails);

    ProductDto findProduct(String productId);

    List<ProductDto> getAllProductsInCategory(String category);

    List<ProductDto> getAllProductContainingProductName(String productName);
}
