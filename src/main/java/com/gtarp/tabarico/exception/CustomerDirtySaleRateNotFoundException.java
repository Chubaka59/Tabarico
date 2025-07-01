package com.gtarp.tabarico.exception;

public class CustomerDirtySaleRateNotFoundException extends NotFoundException {
    public CustomerDirtySaleRateNotFoundException(int id) { super("CustomerDirtySaleRate with id " + id + " not found"); }
}
