package com.gtarp.tabarico.exception;

public class ContractNotFoundException extends NotFoundException {
    public ContractNotFoundException(int id) { super("Contract with id " + id + " not found"); }
    public ContractNotFoundException(String company) { super("Contract for the company " + company + " not found"); }
}
