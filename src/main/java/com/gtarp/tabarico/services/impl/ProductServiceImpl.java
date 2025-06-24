package com.gtarp.tabarico.services.impl;

import com.gtarp.tabarico.dto.ProductDto;
import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.exception.ProductAlreadyExistException;
import com.gtarp.tabarico.exception.ProductNotFoundException;
import com.gtarp.tabarico.repositories.ProductRepository;
import com.gtarp.tabarico.services.AbstractCrudService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl extends AbstractCrudService<Product, ProductRepository, ProductDto> {

    public ProductServiceImpl(ProductRepository repository) {
        super(repository);
    }

    @Override
    public Product getById(Integer id) {
        return this.repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product insert(ProductDto productDto) {
        Optional<Product> existingProduct = this.repository.findProductByName(productDto.getName());
        if (existingProduct.isPresent()) {
            throw new ProductAlreadyExistException(productDto.getName());
        }
        Product newProduct = new Product(productDto);
        return this.repository.save(newProduct);
    }
}
