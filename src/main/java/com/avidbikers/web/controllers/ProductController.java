package com.avidbikers.web.controllers;

import com.avidbikers.data.dto.ProductDto;
import com.avidbikers.services.ProductService;
import com.avidbikers.web.exceptions.ProductException;
import com.avidbikers.web.payload.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/v1/avidbikers/product")
public class ProductController {


    @Autowired
    private ProductService productService;

    @PostMapping("/new")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("Product DTO : {}", productDto);
        productService.addProduct(productDto);
        return new ResponseEntity<>(new ApiResponse(true, "Product created successfully"),
                HttpStatus.CREATED);
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<?> removeProduct(@Valid @PathVariable String productId) {
//        System.out.println(productId);
        log.info("productId : {}", productId);
        try {
            productService.removeProduct(productId);
            return new ResponseEntity<>(new ApiResponse(true, "product removed successfully"),
                    HttpStatus.NO_CONTENT);

        } catch (ProductException productException) {
            return new ResponseEntity<>(new ApiResponse(false, productException.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("{productId}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDto updatedProductDetails, @PathVariable String productId) {
//        System.out.println(updatedProductDetails);
        log.info("Product DTO : {} \n productID : {}", updatedProductDetails, productId);
        try {
            ProductDto updatedProduct = productService.updateProduct(productId, updatedProductDetails);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

        } catch (ProductException productException) {
            return new ResponseEntity<>(new ApiResponse(false, productException.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/all/{categoryId}")
    public ResponseEntity<?> getAllProductsInACategory(@PathVariable String categoryId) {
        List<ProductDto> products = productService.getAllProductsInCategory(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("{productId}")
    public ResponseEntity<?> findProductById(@RequestBody @PathVariable String productId) {
//        System.out.println(productId);
        log.info("productId : {}", productId);
        try {
            ProductDto productDto = productService.findProductById(productId);
            return new ResponseEntity<>(productDto, HttpStatus.OK);
        } catch (ProductException productException) {
            return new ResponseEntity<>(new ApiResponse(false, productException.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }));
        return errors;
    }
}
