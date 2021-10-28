package com.avidbikers.data.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class ProductCategoryDto {
    private String id;
    @NotBlank(message = "Category name cannot be blank")
    private String name;
}
