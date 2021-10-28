package com.avidbikers.web.controllers;

import com.avidbikers.data.dto.ProductCategoryDto;
import com.avidbikers.services.ProductCategoryService;
import com.avidbikers.web.exceptions.ProductCategoryException;
import com.avidbikers.web.payload.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/avidbikers/category")
@Slf4j
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @PostMapping("/new")
    public ResponseEntity<?> newCategory(@RequestBody ProductCategoryDto categoryDTO){
        productCategoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(new ApiResponse(true, "Category created successfully"), HttpStatus.CREATED);
    }
    @GetMapping("/all")
    public ResponseEntity<?> getCategories(){
        List<ProductCategoryDto> categories = productCategoryService.getAllCategory();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    @GetMapping("{categoryId}")
    public ResponseEntity<?> getRoleById(@PathVariable String categoryId){
        try {
            ProductCategoryDto productCategoryDto = productCategoryService.getCategoryById(categoryId);
            return new ResponseEntity<>(productCategoryDto, HttpStatus.OK);
        } catch (ProductCategoryException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}