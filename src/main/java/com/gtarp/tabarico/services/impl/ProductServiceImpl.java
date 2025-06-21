package com.gtarp.tabarico.services.impl;

import com.gtarp.tabarico.dto.ProductDto;
import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.exception.ProductAlreadyExistException;
import com.gtarp.tabarico.exception.ProductNotFoundException;
import com.gtarp.tabarico.repositories.ProductRepository;
import com.gtarp.tabarico.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    @Autowired
    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product addProduct(ProductDto productDto) {
        Optional<Product> existingProduct = productRepository.findProductByName(productDto.getName());
        if (existingProduct.isPresent()) {
            throw new ProductAlreadyExistException(productDto.getName());
        }
        Product newProduct = new Product(productDto);
        return productRepository.save(newProduct);
    }

    @Override
    public Product updateProduct(int id, ProductDto productDto) {
        Product updatedProduct = getProductById(id).update(productDto);
        return productRepository.save(updatedProduct);
    }

    @Override
    public void deleteProduct(int id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
