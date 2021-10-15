package com.avidbikers.services;

import com.avidbikers.data.dto.ProductDto;
import com.avidbikers.data.model.Product;
import com.avidbikers.data.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void removeProduct(String productId) {

    }

    @Override
    public void removeProduct(String productId, int quantity) {

    }

    @Override
    public ProductDto findProductById(String productId) {
        return null;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return null;
    }

    @Override
    public ProductDto updateProduct(String productId, ProductDto updatedProductDetails) {
        return null;
    }

    @Override
    public ProductDto findProduct(String productId) {
        return null;
    }

    @Override
    public List<ProductDto> getAllProductsInCategory(String category) {
        return null;
    }

    @Override
    public List<ProductDto> getAllProductContainingProductName(String productName) {
        return null;
    }
}
