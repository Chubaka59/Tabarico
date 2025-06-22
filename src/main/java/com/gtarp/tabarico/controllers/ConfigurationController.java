package com.gtarp.tabarico.controllers;

import com.gtarp.tabarico.dto.ContractDto;
import com.gtarp.tabarico.dto.ProductDto;
import com.gtarp.tabarico.services.ContractService;
import com.gtarp.tabarico.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ConfigurationController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ContractService contractService;

    @GetMapping("/configuration")
    public String getConfigurationPage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("contracts", contractService.getAllContracts());
        return "configuration";
    }

    @GetMapping("/configuration/addproduct")
    public String showAddProductPage(ProductDto productDto) {
        return "addProduct";
    }

    @PostMapping("/configuration/products")
    public String addProduct(@Valid @ModelAttribute("productDto") ProductDto productDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("products", productDto);
            return showAddProductPage(productDto);
        }
        try {
            productService.addProduct(productDto);
            return getConfigurationPage(model);
        } catch (Exception e) {
            return "addProduct";
        }
    }

    @GetMapping("/configuration/products/{id}/delete")
    public String deleteProduct(@PathVariable Integer id, Model model) {
        productService.deleteProduct(id);
        return getConfigurationPage(model);
    }

    @GetMapping("/configuration/products/{id}")
    public String showUpdateProductPage(@PathVariable("id") Integer id, Model model) {
        if(!model.containsAttribute("product")) {
            model.addAttribute("product", productService.getProductById(id));
        }
        return "updateProduct";
    }

    @PostMapping("/configuration/products/{id}")
    public String updateProduct(@PathVariable("id") Integer id, @Valid @ModelAttribute("productDto") ProductDto productDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", productDto);
            return "updateProduct";
        }
        try {
            productService.updateProduct(id, productDto);
            return getConfigurationPage(model);
        } catch (Exception e) {
            return showUpdateProductPage(id, model);
        }
    }

    @GetMapping("/configuration/addContract")
    public String showAddContractPage(ContractDto contractDto) {
        return "addContract";
    }

    @PostMapping("/configuration/contracts")
    public String addContract(@Valid @ModelAttribute("contractDto") ContractDto contractDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("contract", contractDto);
            return showAddContractPage(contractDto);
        }
        try {
            contractService.addContract(contractDto);
            return getConfigurationPage(model);
        } catch (Exception e) {
            return showAddContractPage(contractDto);
        }
    }

    @GetMapping("/configuration/contracts/{id}/delete")
    public String deleteContract(@PathVariable Integer id, Model model) {
        contractService.deleteContract(id);
        return getConfigurationPage(model);
    }

    @GetMapping("/configuration/contracts/{id}")
    public String showUpdateContractPage(@PathVariable Integer id, Model model) {
        model.addAttribute("contract", contractService.getContractById(id));
        return "updateContract";
    }

    @PostMapping("/configuration/contracts/{id}")
    public String updateContract(@PathVariable("id") Integer id, @Valid @ModelAttribute("contractDto") ContractDto contractDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("contract", contractDto);
            return "updateContract";
        }
        try {
            contractService.updateContract(id, contractDto);
            return getConfigurationPage(model);
        } catch (Exception e) {
            return showUpdateContractPage(id, model);
        }
    }
}
