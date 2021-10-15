package com.avidbikers.data.repository;

import com.avidbikers.data.model.ProductCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String> {
}
