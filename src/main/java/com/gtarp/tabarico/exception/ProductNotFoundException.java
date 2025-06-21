package com.gtarp.tabarico.exception;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(int id) { super("Product with id " + id + " not found"); }
    public ProductNotFoundException(String name) { super("Product with the name " + name + " not found"); }
}
