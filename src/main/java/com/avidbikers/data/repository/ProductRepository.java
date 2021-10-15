package com.avidbikers.data.repository;

import com.avidbikers.data.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findProductByPriceGreaterThan(BigDecimal price);

    List<Product> findProductByDescriptionContaining(String phrase);

    List<Product> findProductsByCategoryId(String productCategoryId);

    List<Product> findAllByName(String productName);
}
