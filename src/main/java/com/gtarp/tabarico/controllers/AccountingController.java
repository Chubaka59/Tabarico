package com.gtarp.tabarico.controllers;

import com.gtarp.tabarico.dto.ContractDto;
import com.gtarp.tabarico.dto.ProductDto;
import com.gtarp.tabarico.dto.accouting.CustomerSaleDto;
import com.gtarp.tabarico.dto.accouting.ExporterSaleDto;
import com.gtarp.tabarico.dto.accouting.StockDto;
import com.gtarp.tabarico.entities.Contract;
import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.entities.accounting.OperationStock;
import com.gtarp.tabarico.entities.accounting.TypeOfSale;
import com.gtarp.tabarico.entities.accounting.TypeOfStockMovement;
import com.gtarp.tabarico.services.AccountingService;
import com.gtarp.tabarico.services.CrudService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

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
            accountingService.createExporterSale(exporterSaleDto, principal.getName());
            return getPersonalDashboardPage(model, principal);
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
            model.addAttribute("customerSaleDto", customerSaleDto);
            return getAddCustomerSalePage(customerSaleDto, model);
        }
        try {
            accountingService.createCustomerSale(customerSaleDto, principal.getName());
            return getPersonalDashboardPage(model, principal);
        } catch (Exception e) {
            return "addCustomerSale";
        }
    }

    @GetMapping("/modifyStock")
    public String getModifyStockPage(@RequestParam(required = false) Optional<LocalDate> date, StockDto stockDto, Model model) {
        model.addAttribute("products", productService.getAll());
        model.addAttribute("operationStocks", OperationStock.values());
        LocalDate selectedDate = date.orElseGet(LocalDate::now);
        model.addAttribute("stocks", accountingService.getStockListByDate(selectedDate));
        return "modifyStock";
    }

    @PostMapping("/modifyStock")
    public String modifyStock(@Valid @ModelAttribute("stockDto") StockDto stockDto, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            model.addAttribute("stockDto", stockDto);
            return getModifyStockPage(Optional.empty(), stockDto, model);
        }
        try {
            stockDto.setTypeOfStockMovement(TypeOfStockMovement.stockModification);
            accountingService.modifyStock(stockDto, principal.getName());
            return getPersonalDashboardPage(model, principal);
        } catch (Exception e) {
            return getModifyStockPage(Optional.empty(), stockDto, model);
        }
    }

    @GetMapping("/dashboard")
    public String getDashboardPage(Model model) {
        model.addAttribute("dashboardDtoList", accountingService.getDashboardListOfThisWeek());
        return "dashboard";
    }

    @RequestMapping("/personalDashboard")
    public String getPersonalDashboardPage(Model model, Principal principal) {
        model.addAttribute("personalDashboardDto", accountingService.getPersonalDashboardDto(principal.getName()));
        return "personalDashboard";
    }
}
