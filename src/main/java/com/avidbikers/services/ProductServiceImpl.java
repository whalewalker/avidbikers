package com.avidbikers.services;

import com.avidbikers.data.dto.ProductDto;
import com.avidbikers.data.model.Product;
import com.avidbikers.data.repository.ProductRepository;
import com.avidbikers.web.exceptions.ProductException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ModelMapper mapper;

    @Override
    public void addProduct(ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);
        addNewProduct(product);
    }

    private void addNewProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void removeProduct(String productId) throws ProductException {
        Product productToRemove = findAProductById(productId);
        removeAProduct(productToRemove);
    }

    private void removeAProduct(Product product) {
        productRepository.delete(product);
    }

    private Product findAProductById(String productId) throws ProductException {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("No user found with id" + productId));
    }

    @Override
    public void removeProduct(String productId, int quantity) {

    }

    @Override
    public Product findProduct(String productId) throws ProductException {
        return findAProductById(productId);
    }

    @Override
    public ProductDto findProductById(String productId) throws ProductException {
        Product product = findAProductById(productId);
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<ProductDto> products = new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            products.add(mapper.map(product, ProductDto.class));
        }
        return products;
    }

    @Override
    public ProductDto updateProduct(String productId, ProductDto updatedProductDetails) throws ProductException {
        Product productToUpdate = findAProductById(productId);
        mapper.map(updatedProductDetails, productToUpdate);
        Product updatedProduct = productRepository.save(productToUpdate);
        return mapper.map(updatedProduct, ProductDto.class);
    }


    @Override
    public List<ProductDto> getAllProductsInCategory(String category) {
        List<ProductDto> products = new ArrayList<>();
        for (Product product: productRepository.findProductsByCategoryId(category)){
            products.add(mapper.map(product, ProductDto.class));
        }
        return products;
    }

    @Override
    public List<ProductDto> getAllProductContainingProductName(String productName) {
        List<ProductDto> products = new ArrayList<>();
        for (Product product: productRepository.findAllByName(productName)){
            products.add(mapper.map(product, ProductDto.class));
        }
        return products;
    }

    @Override
    public List<ProductDto> getAllProductContainingProductDesc(String phrase) {
        List<ProductDto> products = new ArrayList<>();
        for (Product product: productRepository.findProductByDescriptionContaining(phrase)){
            products.add(mapper.map(product, ProductDto.class));
        }
        return products;
    }
}
