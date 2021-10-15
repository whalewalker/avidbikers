package com.avidbikers.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    @NotBlank(message = "Please enter a product description")
    private String description;

    @NotBlank(message = "Please enter a product name")
    private String name;

    @NotBlank(message = "please enter a price")
    private BigDecimal price;

    @NotBlank(message = "Please provide a product Image")
    private String image;

    @NotBlank(message = "Please provide a category Id")
    private String categoryId;

}
