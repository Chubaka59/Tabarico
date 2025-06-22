package com.gtarp.tabarico.controllers;

import com.gtarp.tabarico.dto.ContractDto;
import com.gtarp.tabarico.dto.ProductDto;
import com.gtarp.tabarico.entities.Contract;
import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.services.ContractService;
import com.gtarp.tabarico.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ConfigurationControllerTest {
    @InjectMocks
    ConfigurationController configurationController;

    @Mock
    private Model model;
    @Mock
    private BindingResult result;
    @Mock
    private ProductService productService;
    @Mock
    private ContractService contractService;

    @Test
    public void getConfigurationPageTest() {
        //GIVEN we should get this string and a list of products and contracts
        String expectedString = "configuration";
        when(productService.getAllProducts()).thenReturn(new ArrayList<>());
        when(contractService.getAllContracts()).thenReturn(new ArrayList<>());

        //WHEN we call this method
        String actualString = configurationController.getConfigurationPage(model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void showAddProductPageTest() {
        //GIVEN we should get this string
        String expectedString = "addProduct";

        //WHEN we call this method
        String actualString = configurationController.showAddProductPage(new ProductDto());

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void addProductTest() {
        //GIVEN we should get this string and a product should be added
        String expectedString = "configuration";
        when(productService.addProduct(any(ProductDto.class))).thenReturn(new Product());
        when(result.hasErrors()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(new ArrayList<>());
        when(contractService.getAllContracts()).thenReturn(new ArrayList<>());

        //WHEN we call this method
        String actualString = configurationController.addProduct(new ProductDto(), result, model);

        //THEN we get the correct string and the product is added
        assertEquals(expectedString, actualString);
        verify(productService).addProduct(any(ProductDto.class));
    }

    @Test
    public void addProductWhenErrorInTheFormTest() {
        //GIVEN there is an error in the form
        String expectedString = "addProduct";
        when(result.hasErrors()).thenReturn(true);

        //WHEN we try to add a product
        String actualString = configurationController.addProduct(new ProductDto(), result, model);

        //THEN we get the correct string and the user is not created
        assertEquals(expectedString, actualString);
        verify(productService, times(0)).addProduct(any(ProductDto.class));
    }

    @Test
    public void addProductWhenAnExceptionIsThrownTest() {
        //GIVEN an exception will be thrown
        String expectedString = "addProduct";
        when(productService.addProduct(any(ProductDto.class))).thenThrow(new RuntimeException());

        //WHEN we try to add the product
        String actualString = configurationController.addProduct(new ProductDto(), result, model);

        //THEN we get the correct string and the method to add a product has been called
        assertEquals(expectedString, actualString);
        verify(productService).addProduct(any(ProductDto.class));
    }

    @Test
    public void deleteProductTest() {
        //GIVEN we would try to delete a product
        String expectedString = "configuration";
        doNothing().when(productService).deleteProduct(anyInt());

        //WHEN we try to delete a product
        String actualString = configurationController.deleteProduct(1, model);

        //THEN we get the correct string and the product is deleted
        assertEquals(expectedString, actualString);
        verify(productService, times(1)).deleteProduct(anyInt());
    }

    @Test
    public void showUpdateProductPageTest() {
        //GIVEN we should get this string and get a product
        String expectedString = "updateProduct";
        when(productService.getProductById(anyInt())).thenReturn(new Product());

        //WHEN we call the method
        String actualString = configurationController.showUpdateProductPage(1, model);

        //THEN we get the correct string and the informations of the product to update are called
        assertEquals(expectedString, actualString);
        verify(productService, times(1)).getProductById(anyInt());
    }

    @Test
    public void updateProductTest() {
        //GIVEN we should get this string and it should update the product
        String expectedString = "configuration";
        when(result.hasErrors()).thenReturn(false);
        when(productService.updateProduct(anyInt(), any(ProductDto.class))).thenReturn(new Product());

        //WHEN we call this method
        String actualString = configurationController.updateProduct(1, new ProductDto(), result, model);

        //THEN we get the correct string and the product has been updated
        assertEquals(expectedString, actualString);
        verify(productService, times(1)).updateProduct(anyInt(), any(ProductDto.class));
    }

    @Test
    public void updateProductWhenErrorInTheFormTest() {
        //GIVEN we should get this string as there is an error in the form
        String expectedString = "updateProduct";
        when(result.hasErrors()).thenReturn(true);

        //WHEN we call this method
        String actualString = configurationController.updateProduct(1, new ProductDto(), result, model);

        //THEN we get the correct string and the product isn't updated
        assertEquals(expectedString, actualString);
        verify(productService, times(0)).updateProduct(anyInt(), any(ProductDto.class));
    }

    @Test
    public void updateProductWhenAnExceptionIsThrownTest() {
        //GIVEN an exception should be thrown
        String expectedString = "updateProduct";
        when(result.hasErrors()).thenReturn(false);
        when(productService.updateProduct(anyInt(), any(ProductDto.class))).thenThrow(new RuntimeException());

        //WHEN we call this method
        String actualString = configurationController.updateProduct(1, new ProductDto(), result, model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
        verify(productService, times(1)).updateProduct(anyInt(), any(ProductDto.class));
    }

    //*************************************

    @Test
    public void showAddContractPageTest() {
        //GIVEN we should get this string
        String expectedString = "addContract";

        //WHEN we call this method
        String actualString = configurationController.showAddContractPage(new ContractDto());

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void addContractTest() {
        //GIVEN we should get this string and a contract should be added
        String expectedString = "configuration";
        when(contractService.addContract(any(ContractDto.class))).thenReturn(new Contract());
        when(result.hasErrors()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(new ArrayList<>());
        when(contractService.getAllContracts()).thenReturn(new ArrayList<>());

        //WHEN we call this method
        String actualString = configurationController.addContract(new ContractDto(), result, model);

        //THEN we get the correct string and the product is added
        assertEquals(expectedString, actualString);
        verify(contractService).addContract(any(ContractDto.class));
    }

    @Test
    public void addContractWhenErrorInTheFormTest() {
        //GIVEN there is an error in the form
        String expectedString = "addContract";
        when(result.hasErrors()).thenReturn(true);

        //WHEN we try to add a contract
        String actualString = configurationController.addContract(new ContractDto(), result, model);

        //THEN we get the correct string and the contract is not created
        assertEquals(expectedString, actualString);
        verify(contractService, times(0)).addContract(any(ContractDto.class));
    }

    @Test
    public void addContractWhenAnExceptionIsThrownTest() {
        //GIVEN an exception will be thrown
        String expectedString = "addContract";
        when(contractService.addContract(any(ContractDto.class))).thenThrow(new RuntimeException());

        //WHEN we try to add the contract
        String actualString = configurationController.addContract(new ContractDto(), result, model);

        //THEN we get the correct string and the method to add a product has been called
        assertEquals(expectedString, actualString);
        verify(contractService).addContract(any(ContractDto.class));
    }

    @Test
    public void deleteContractTest() {
        //GIVEN we would try to delete a product
        String expectedString = "configuration";
        doNothing().when(contractService).deleteContract(anyInt());

        //WHEN we try to delete a contract
        String actualString = configurationController.deleteContract(1, model);

        //THEN we get the correct string and the contract is deleted
        assertEquals(expectedString, actualString);
        verify(contractService, times(1)).deleteContract(anyInt());
    }

    @Test
    public void showUpdateContractPageTest() {
        //GIVEN we should get this string and get a contract
        String expectedString = "updateContract";
        when(contractService.getContractById(anyInt())).thenReturn(new Contract());

        //WHEN we call the method
        String actualString = configurationController.showUpdateContractPage(1, model);

        //THEN we get the correct string and the informations of the contract to update are called
        assertEquals(expectedString, actualString);
        verify(contractService, times(1)).getContractById(anyInt());
    }

    @Test
    public void updateContractTest() {
        //GIVEN we should get this string and it should update the contract
        String expectedString = "configuration";
        when(result.hasErrors()).thenReturn(false);
        when(contractService.updateContract(anyInt(), any(ContractDto.class))).thenReturn(new Contract());

        //WHEN we call this method
        String actualString = configurationController.updateContract(1, new ContractDto(), result, model);

        //THEN we get the correct string and the product has been updated
        assertEquals(expectedString, actualString);
        verify(contractService, times(1)).updateContract(anyInt(), any(ContractDto.class));
    }

    @Test
    public void updateContractWhenErrorInTheFormTest() {
        //GIVEN we should get this string as there is an error in the form
        String expectedString = "updateContract";
        when(result.hasErrors()).thenReturn(true);

        //WHEN we call this method
        String actualString = configurationController.updateContract(1, new ContractDto(), result, model);

        //THEN we get the correct string and the contract isn't updated
        assertEquals(expectedString, actualString);
        verify(contractService, times(0)).updateContract(anyInt(), any(ContractDto.class));
    }

    @Test
    public void updateContractWhenAnExceptionIsThrownTest() {
        //GIVEN an exception should be thrown
        String expectedString = "updateContract";
        when(result.hasErrors()).thenReturn(false);
        when(contractService.updateContract(anyInt(), any(ContractDto.class))).thenThrow(new RuntimeException());

        //WHEN we call this method
        String actualString = configurationController.updateContract(1, new ContractDto(), result, model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
        verify(contractService, times(1)).updateContract(anyInt(), any(ContractDto.class));
    }

}
