package com.avidbikers.services;


import com.avidbikers.data.dto.ProductCategoryDto;
import com.avidbikers.data.model.ProductCategory;
import com.avidbikers.web.exceptions.ProductCategoryException;

import java.util.List;

public interface ProductCategoryService {
    void createCategory(ProductCategoryDto category);

    ProductCategoryDto getCategoryById(String categoryId) throws ProductCategoryException;

    ProductCategoryDto updateCategory(String categoryId, ProductCategoryDto productCategoryDto) throws ProductCategoryException;

    void deleteCategory(String categoryId);

    List<ProductCategoryDto> getAllCategory();

    ProductCategory getProductCategoryById(String id) throws ProductCategoryException;
}
