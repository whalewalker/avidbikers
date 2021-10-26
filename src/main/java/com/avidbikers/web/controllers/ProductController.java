package com.avidbikers.web.controllers;

import com.avidbikers.data.dto.ProductDto;
import com.avidbikers.services.ProductService;
import com.avidbikers.web.payload.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/v1/avidbikers/product")
@Slf4j
public class ProductController {

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ROLE_VENDOR')")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("Product DTO : {}", productDto);
        productService.addProduct(productDto);
        return new ResponseEntity<>(new ApiResponse(true, "Product created successfully"), HttpStatus.CREATED);
    }


    @Autowired
    private ProductService productService;
}
