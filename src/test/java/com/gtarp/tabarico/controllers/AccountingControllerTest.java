package com.gtarp.tabarico.controllers;

import com.gtarp.tabarico.dto.ContractDto;
import com.gtarp.tabarico.dto.ProductDto;
import com.gtarp.tabarico.dto.accouting.CustomerSaleDto;
import com.gtarp.tabarico.dto.accouting.ExporterSaleDto;
import com.gtarp.tabarico.dto.accouting.StockDto;
import com.gtarp.tabarico.entities.Contract;
import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.entities.accounting.CustomerSale;
import com.gtarp.tabarico.entities.accounting.ExporterSale;
import com.gtarp.tabarico.entities.accounting.Stock;
import com.gtarp.tabarico.services.AccountingService;
import com.gtarp.tabarico.services.CrudService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AccountingControllerTest {
    @InjectMocks
    private AccountingController accountingController;

    @Mock
    private Model model;
    @Mock
    private BindingResult result;
    @Mock
    private AccountingService accountingService;
    @Mock
    private CrudService<Product, ProductDto> productService;
    @Mock
    private CrudService<Contract, ContractDto> contractService;
    @Mock
    private Principal principal;

    @Test
    public void getAddExporterSalePageTest() {
        //GIVEN we should get this string
        String expectedString = "addExporterSale";
        ExporterSaleDto exporterSaleDto = new ExporterSaleDto();

        //WHEN we call this method
        String actualString = accountingController.getAddExporterSalePage(exporterSaleDto, model);

        //THEN we get the correct String
        assertEquals(expectedString, actualString);
    }

    @Test
    public void AddExporterSaleTest() {
        //GIVEN we should get this string and the exporterSale should be created
        String expectedString = "home";
        when(accountingService.createExporterSale(any(ExporterSaleDto.class), anyString())).thenReturn(new ExporterSale());
        when(principal.getName()).thenReturn("principal");

        //WHEN we try to create the exporter sale
        String actualString = accountingController.addExporterSale(new ExporterSaleDto(), result, model, principal);

        //THEN we get the correct string and the sale has been created
        assertEquals(expectedString, actualString);
        verify(accountingService, times(1)).createExporterSale(any(ExporterSaleDto.class), anyString());
    }

    @Test
    public void AddExporterSaleWhenErrorInTheFormTest() {
        //GIVEN there is an error in the form and we should get this string
        String expectedString = "addExporterSale";
        when(result.hasErrors()).thenReturn(true);

        //WHEN we call this method
        String actualString = accountingController.addExporterSale(new ExporterSaleDto(), result, model, principal);

        //THEN we get the correct string and the exporterSale hasn't been created
        assertEquals(expectedString, actualString);
        verify(accountingService, times(0)).createExporterSale(any(ExporterSaleDto.class), anyString());
    }

    @Test
    public void addExporterSaleWhenExceptionIsThrownTest() {
        //GIVEN an exception should be thrown and we should get this string
        String expectedString = "addExporterSale";
        when(accountingService.createExporterSale(any(ExporterSaleDto.class), anyString())).thenThrow(new RuntimeException());
        when(principal.getName()).thenReturn("principal");

        //WHEN we call this method
        String actualString = accountingController.addExporterSale(new ExporterSaleDto(), result, model, principal);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
        verify(accountingService, times(1)).createExporterSale(any(ExporterSaleDto.class), anyString());
    }

    @Test
    public void getAddCustomerSalePageTest() {
        //GIVEN we should get this string and a list of product and contract
        String expectedString = "addCustomerSale";
        when(productService.getAll()).thenReturn(new ArrayList<>());
        when(contractService.getAll()).thenReturn(new ArrayList<>());

        //WHEN we call this method
        String actualString = accountingController.getAddCustomerSalePage(new CustomerSaleDto(), model);

        //THEN we get the correct string and we got the list of products and contracrs
        assertEquals(expectedString, actualString);
        verify(productService, times(1)).getAll();
        verify(contractService, times(1)).getAll();
    }

    @Test
    public void addCustomerSaleTest() {
        //GIVEN we should ge this string and a customerSale should be created
        String expectedString = "home";
        when(accountingService.createCustomerSale(any(CustomerSaleDto.class), anyString())).thenReturn(new CustomerSale());
        when(principal.getName()).thenReturn("principal");

        //WHEN we call this method
        String actualString = accountingController.addCustomerSale(new CustomerSaleDto(), result, model, principal);

        //THEN we get the correct string and a customer sale has been created
        assertEquals(expectedString, actualString);
        verify(accountingService, times(1)).createCustomerSale(any(CustomerSaleDto.class), anyString());
    }

    @Test
    public void addCustomerSaleWhenErrorInTheFormTest() {
        //GIVEN there is an error in the form
        String expectedString = "addCustomerSale";
        when(result.hasErrors()).thenReturn(true);

        //WHEN we call this method
        String actualString = accountingController.addCustomerSale(new CustomerSaleDto(), result, model, principal);

        //THEN we get the correct string and the customerSale isn't created
        assertEquals(expectedString, actualString);
        verify(accountingService, times(0)).createCustomerSale(any(CustomerSaleDto.class), anyString());
    }

    @Test
    public void addCustomerSaleWhenExceptionIsThrownTest() {
        //GIVEN we should get this string as an error should be thrown
        String expectedString = "addCustomerSale";
        when(accountingService.createCustomerSale(any(CustomerSaleDto.class), anyString())).thenThrow(new RuntimeException());
        when(principal.getName()).thenReturn("principal");

        //WHEN we call this method
        String actualString = accountingController.addCustomerSale(new CustomerSaleDto(), result, model, principal);

        //THEN we get the correct string and the customer sale has tried to be created
        assertEquals(expectedString, actualString);
        verify(accountingService, times(1)).createCustomerSale(any(CustomerSaleDto.class), anyString());
    }

    @Test
    public void getModifyStockPageTest() {
        //GIVEN we should get this string and a list of product and date
        String expectedString = "modifyStock";
        when(productService.getAll()).thenReturn(new ArrayList<>());
        when(accountingService.getStockListByDate(any(LocalDate.class))).thenReturn(new ArrayList<>());

        //WHEN we call this method
        String actualString = accountingController.getModifyStockPage(Optional.of(LocalDate.now()), new StockDto(), model);

        //THEN we get the correct string and the list of product and stock
        assertEquals(expectedString, actualString);
        verify(productService, times(1)).getAll();
        verify(accountingService, times(1)).getStockListByDate(any(LocalDate.class));
    }

    @Test
    public void modifyStockTest() {
        //GIVEN we should get this string and the stock should be modified
        String expectedString = "home";
        when(accountingService.modifyStock(any(StockDto.class), anyString())).thenReturn(new Stock());
        when(principal.getName()).thenReturn("principal");

        //WHEN we call this method
        String actualString = accountingController.modifyStock(new StockDto(), result, model, principal);

        //THEN we get the correct string and the stock has been modified
        assertEquals(expectedString, actualString);
        verify(accountingService, times(1)).modifyStock(any(StockDto.class), anyString());
    }

    @Test
    public void modifyStockWhenErrorInTheFormTest() {
        //GIVEN we should this string as there is an error in the form
        String expectedString = "modifyStock";
        when(result.hasErrors()).thenReturn(true);

        //WHEN we call this method
        String actualString = accountingController.modifyStock(new StockDto(), result, model, principal);

        //THEN we get the correct string and the stock hasn't been modified
        assertEquals(expectedString, actualString);
        verify(accountingService, times(0)).modifyStock(any(StockDto.class), anyString());
    }

    @Test
    public void modifyStockWhenExceptionIsThrownTest() {
        //GIVEN we should get this string as an error should be thrown
        String expectedString = "modifyStock";
        when(accountingService.modifyStock(any(StockDto.class), anyString())).thenThrow(new RuntimeException());
        when(principal.getName()).thenReturn("principal");

        //WHEN we call this method
        String actualString = accountingController.modifyStock(new StockDto(), result, model, principal);

        //THEN we get the correct string and the stock has tried to be modified
        assertEquals(expectedString, actualString);
        verify(accountingService, times(1)).modifyStock(any(StockDto.class), anyString());
    }

    @Test
    public void getAccountingSummaryPageTest() {
        //GIVEN we should get this string
        String expectedString = "accountingSummary";
        when(accountingService.getAccountingSummaryListOfThisWeek()).thenReturn(new ArrayList<>());

        //WHEN we call this method
        String actualString = accountingController.getAccountingSummaryPage(model);

        //THEN we get the correct string and we get a list of accounting
        assertEquals(expectedString, actualString);
        verify(accountingService, times(1)).getAccountingSummaryListOfThisWeek();
    }
}
