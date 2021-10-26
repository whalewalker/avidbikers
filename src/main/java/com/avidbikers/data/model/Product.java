package com.avidbikers.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class Product {
    @Id
    private String id;
    private String name;
    private BigDecimal price;
    private String description;
    private String image;
    @DBRef
    private List<Review> reviews = new ArrayList<>();
    private String categoryId;
}
