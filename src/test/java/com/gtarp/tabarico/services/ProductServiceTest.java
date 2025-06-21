package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.ProductDto;
import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.exception.ProductAlreadyExistException;
import com.gtarp.tabarico.exception.ProductNotFoundException;
import com.gtarp.tabarico.repositories.ProductRepository;
import com.gtarp.tabarico.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    @MockBean
    private ProductService productService;

    private final ProductRepository productRepository = mock(ProductRepository.class);

    @BeforeEach
    public void setUpPerTest() {
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    public void getAllProductsTest() {
        //GIVEN this should return a list
        List<Product> expectedProductList = new ArrayList<>();
        when(productRepository.findAll()).thenReturn(expectedProductList);

        //WHEN we call the method
        List<Product> actualProductList = productService.getAllProducts();

        //THEN the correct method is called and we get the correct return
        assertEquals(expectedProductList, actualProductList);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void getProductByIdTest() {
        //GIVEN this should return a product
        Product expectedProduct = new Product();
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(expectedProduct));

        //WHEN we try to get this product
        Product actualProduct = productService.getProductById(1);

        //THEN productRepository.findById is called and we get the correct return
        assertEquals(expectedProduct, actualProduct);
        verify(productRepository, times(1)).findById(anyInt());
    }

    @Test
    public void getProductByIdWhenProductIsNotFoundTest() {
        //GIVEN this should not find a product
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        //WHEN we try to get this product THEN an exception is thrown
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1));
    }

    @Test
    public void addProductTest() {
        //GIVEN the product we would add doesn't exist
        when(productRepository.findProductByName(anyString())).thenReturn(Optional.empty());
        ProductDto productDto = new ProductDto(1, "testProduct", 100, 50);
        Product product = new Product();
        when(productRepository.save(any(Product.class))).thenReturn(product);

        //WHEN we try to add this product
        productService.addProduct(productDto);

        //THEN productRepository.save is called
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void addProductWhenProductAlreadyExistsTest() {
        //GIVEN the product we would add already exist
        Product product = new Product();
        ProductDto productDto = new ProductDto(1, "testProduct", 100, 50);
        when(productRepository.findProductByName(anyString())).thenReturn(Optional.of(product));

        //WHEN we try to add the product THEN an exception is thrown
        assertThrows(ProductAlreadyExistException.class, () -> productService.addProduct(productDto));
    }

    @Test
    public void updateProductTest() {
        //GIVEN there is a product to update
        Product existingProduct = new Product();
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(existingProduct));
        ProductDto productDto = new ProductDto(1, "testProduct", 100, 50);

        //WHEN we try to update the product
        productService.updateProduct(1, productDto);

        //THEN productRepository.save is called
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void deleteProductTest() {
        //GIVEN there is a product to delete
        Product existingProduct = new Product();
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(existingProduct));
        doNothing().when(productRepository).delete(any(Product.class));

        //WHEN we try to delete the product
        productService.deleteProduct(1);

        //THEN productRepository.delete is called
        verify(productRepository, times(1)).delete(any(Product.class));
    }
}
