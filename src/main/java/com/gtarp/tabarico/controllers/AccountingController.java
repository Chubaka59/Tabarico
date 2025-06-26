package com.gtarp.tabarico.controllers;

import com.gtarp.tabarico.dto.ContractDto;
import com.gtarp.tabarico.dto.ProductDto;
import com.gtarp.tabarico.dto.accouting.CustomerSaleDto;
import com.gtarp.tabarico.dto.accouting.ExporterSaleDto;
import com.gtarp.tabarico.entities.Contract;
import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.entities.accounting.TypeOfSale;
import com.gtarp.tabarico.services.AccountingService;
import com.gtarp.tabarico.services.CrudService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class AccountingController {
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private CrudService<Product, ProductDto> productService;
    @Autowired
    private CrudService<Contract, ContractDto> contractService;

    @GetMapping("/addExporterSale")
    public String getAddExporterSalePage(ExporterSaleDto exporterSaleDto, Model model) {
        return "addExporterSale";
    }

    @PostMapping("/addExporterSale")
    public String addExporterSale(@Valid @ModelAttribute("exporterSaleDto") ExporterSaleDto exporterSaleDto, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            model.addAttribute("exporterSale", exporterSaleDto);
            return "addExporterSale";
        }
        try {
            accountingService.createExporterSale(exporterSaleDto, principal);
            return "home";
        } catch (Exception e) {
            return "addExporterSale";
        }
    }

    @GetMapping("/addCustomerSale")
    public String getAddCustomerSalePage(CustomerSaleDto customerSaleDto, Model model) {
        model.addAttribute("products", productService.getAll());
        model.addAttribute("typeOfSales", TypeOfSale.values());
        model.addAttribute("contracts", contractService.getAll());
        return "addCustomerSale";
    }

    @PostMapping("/addCustomerSale")
    public String addCustomerSale(@Valid @ModelAttribute("customerSaleDto") CustomerSaleDto customerSaleDto, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            model.addAttribute("customerSale", customerSaleDto);
            return "addCustomerSale";
        }
        try {
            accountingService.createCustomerSale(customerSaleDto, principal);
            return "home";
        } catch (Exception e) {
            return "addCustomerSale";
        }
    }
}
