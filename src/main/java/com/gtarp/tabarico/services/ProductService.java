package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.ProductDto;
import com.gtarp.tabarico.entities.Product;

import java.util.List;

public interface ProductService {
    /**
     * Get a list of all products
     * @return a list of product
     */
    List<Product> getAllProducts();

    /**
     * get a product by its id
     * @param id the id of the product
     * @return a product
     */
    Product getProductById(int id);

    /**
     * add a product to the database
     * @param productDto the information of the product to add
     * @return the new product
     */
    Product addProduct(ProductDto productDto);

    /**
     * update the information of a product
     * @param id the id of the product to update
     * @param productDto the information of the product
     * @return the updated product
     */
    Product updateProduct(int id, ProductDto productDto);

    /**
     * delete a product from the database
     * @param id the id of the product to delete
     */
    void deleteProduct(int id);
}
