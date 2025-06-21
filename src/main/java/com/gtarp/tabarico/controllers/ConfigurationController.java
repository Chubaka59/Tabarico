package com.gtarp.tabarico.controllers;

import com.gtarp.tabarico.services.ContractService;
import com.gtarp.tabarico.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
