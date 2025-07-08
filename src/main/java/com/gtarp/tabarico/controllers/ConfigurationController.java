package com.gtarp.tabarico.controllers;

import com.gtarp.tabarico.dto.ContractDto;
import com.gtarp.tabarico.dto.CustomerDirtySaleRateDto;
import com.gtarp.tabarico.dto.ProductDto;
import com.gtarp.tabarico.dto.RoleDto;
import com.gtarp.tabarico.entities.Contract;
import com.gtarp.tabarico.entities.CustomerDirtySaleRate;
import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.entities.Role;
import com.gtarp.tabarico.services.CrudService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class ConfigurationController {
    @Autowired
    private CrudService<Product, ProductDto> productService;
    @Autowired
    private CrudService<Contract, ContractDto> contractService;
    @Autowired
    private CrudService<Role, RoleDto> roleService;
    @Autowired
        private CrudService<CustomerDirtySaleRate, CustomerDirtySaleRateDto> customerDirtySaleRateService;

    @GetMapping("/configuration")
    public String getConfigurationPage(Model model) {
        model.addAttribute("products", productService.getAll());
        model.addAttribute("contracts", contractService.getAll());
        model.addAttribute("roles", roleService.getAll());
        model.addAttribute("customerDirtySaleRates", customerDirtySaleRateService.getAll());
        return "configuration";
    }

    @GetMapping("/configuration/addProduct")
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
            productService.insert(productDto);
            return getConfigurationPage(model);
        } catch (Exception e) {
            log.error("Erreur lors de la création d'un produit", e);
            return "addProduct";
        }
    }

    @GetMapping("/configuration/products/{id}/delete")
    public String deleteProduct(@PathVariable Integer id, Model model) {
        productService.delete(id);
        return getConfigurationPage(model);
    }

    @GetMapping("/configuration/products/{id}")
    public String showUpdateProductPage(@PathVariable("id") Integer id, Model model) {
        if (!model.containsAttribute("productDto")) {
            model.addAttribute("productDto", productService.getById(id));
        }
        return "updateProduct";
    }

    @PostMapping("/configuration/products/{id}")
    public String updateProduct(@PathVariable("id") Integer id, @Valid @ModelAttribute("productDto") ProductDto productDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("productDto", productDto);
            return "updateProduct";
        }
        try {
            productService.update(id, productDto);
            return getConfigurationPage(model);
        } catch (Exception e) {
            log.error("Erreur lors de la modification d'un produit", e);
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
            contractService.insert(contractDto);
            return getConfigurationPage(model);
        } catch (Exception e) {
            log.error("Erreur lors de la création d'un contrat", e);
            return showAddContractPage(contractDto);
        }
    }

    @GetMapping("/configuration/contracts/{id}/delete")
    public String deleteContract(@PathVariable Integer id, Model model) {
        contractService.delete(id);
        return getConfigurationPage(model);
    }

    @GetMapping("/configuration/contracts/{id}")
    public String showUpdateContractPage(@PathVariable Integer id, Model model) {
        if (!model.containsAttribute("contractDto")) {
            model.addAttribute("contractDto", contractService.getById(id));
        }
        return "updateContract";
    }

    @PostMapping("/configuration/contracts/{id}")
    public String updateContract(@PathVariable("id") Integer id, @Valid @ModelAttribute("contractDto") ContractDto contractDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("contractDto", contractDto);
            return "updateContract";
        }
        try {
            contractService.update(id, contractDto);
            return getConfigurationPage(model);
        } catch (Exception e) {
            log.error("Erreur lors de la modification d'un contrat", e);
            return showUpdateContractPage(id, model);
        }
    }

    @GetMapping("/configuration/addRole")
    public String showAddRolePage(RoleDto roleDto) {
        return "addRole";
    }

    @PostMapping("/configuration/roles")
    public String addRole(@Valid @ModelAttribute("roleDto") RoleDto roleDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("role", roleDto);
            return showAddRolePage(roleDto);
        }
        try {
            roleService.insert(roleDto);
            return getConfigurationPage(model);
        } catch (Exception e) {
            log.error("Erreur lors de la création d'un role", e);
            return showAddRolePage(roleDto);
        }
    }

    @GetMapping("/configuration/roles/{id}/delete")
    public String deleteRole(@PathVariable Integer id, Model model) {
        roleService.delete(id);
        return getConfigurationPage(model);
    }

    @GetMapping("/configuration/roles/{id}")
    public String showUpdateRolePage(@PathVariable Integer id, Model model) {
        if (!model.containsAttribute("roleDto")) {
            model.addAttribute("roleDto", roleService.getById(id));
        }
        return "updateRole";
    }

    @PostMapping("/configuration/roles/{id}")
    public String updateRole(@PathVariable("id") Integer id, @Valid @ModelAttribute("roleDto") RoleDto roleDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roleDto", roleDto);
            return "updateRole";
        }
        try {
            roleService.update(id, roleDto);
            return getConfigurationPage(model);
        } catch (Exception e) {
            log.error("Erreur lors de la modification d'un role", e);
            return showUpdateRolePage(id, model);
        }
    }

    @GetMapping("/configuration/customerDirtySaleRates/{id}")
    public String showUpdateCustomerDirtySaleRatePage(@PathVariable Integer id, Model model) {
        if (!model.containsAttribute("customerDirtySaleRateDto")) {
            model.addAttribute("customerDirtySaleRateDto", customerDirtySaleRateService.getById(id));
        }
        return "updateCustomerDirtySaleRate";
    }

    @PostMapping("/configuration/customerDirtySaleRates/{id}")
    public String updateCustomerDirtySaleRate(@PathVariable("id") Integer id, @Valid @ModelAttribute("customerDirtySaleRateDto") CustomerDirtySaleRateDto customerDirtySaleRateDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("customerDirtySaleRateDto", customerDirtySaleRateDto);
            return "updateCustomerDirtySaleRate";
        }
        try {
            customerDirtySaleRateService.update(id, customerDirtySaleRateDto);
            return getConfigurationPage(model);
        } catch (Exception e) {
            log.error("Erreur lors de la modification du taux de redistribution en argent sale", e);
            return showUpdateCustomerDirtySaleRatePage(id, model);
        }
    }
}
