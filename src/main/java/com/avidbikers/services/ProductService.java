package com.avidbikers.services;

import com.avidbikers.data.dto.ProductDto;
import com.avidbikers.data.model.Product;
import com.avidbikers.web.exceptions.ProductException;

import java.util.List;

public interface ProductService {
    void addProduct(ProductDto productDto);

    void removeProduct(String productId) throws ProductException;

    void removeProduct(String productId, int quantity);

     Product findProduct(String productId) throws ProductException;

    ProductDto findProductById(String productId) throws ProductException;

    List<ProductDto> getAllProducts();

    ProductDto updateProduct(String productId, ProductDto updatedProductDetails) throws ProductException;


    List<ProductDto> getAllProductsInCategory(String category);

    List<ProductDto> getAllProductContainingProductName(String productName);

    List<ProductDto> getAllProductContainingProductDesc(String phrase);

}
