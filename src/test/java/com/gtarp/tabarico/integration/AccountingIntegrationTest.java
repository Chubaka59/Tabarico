package com.gtarp.tabarico.integration;

import com.gtarp.tabarico.entities.Contract;
import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.entities.accounting.CustomerSale;
import com.gtarp.tabarico.entities.accounting.ExporterSale;
import com.gtarp.tabarico.repositories.ContractRepository;
import com.gtarp.tabarico.repositories.ProductRepository;
import com.gtarp.tabarico.repositories.accounting.CustomerSaleRepository;
import com.gtarp.tabarico.repositories.accounting.ExporterSaleRepository;
import com.gtarp.tabarico.repositories.accounting.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class AccountingIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ExporterSaleRepository exporterSaleRepository;
    @Autowired
    private CustomerSaleRepository customerSaleRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private StockRepository stockRepository;

    @Test
    @WithUserDetails("testUser")
    public void getAddExporterSalePageTest() throws Exception {
        mockMvc.perform(get("/addExporterSale"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addExporterSale"));
    }

    @Test
    @WithUserDetails("testUser")
    public void addExporterSaleTest() throws Exception {
        int initialCount = exporterSaleRepository.findAll().size();

        mockMvc.perform(post("/addExporterSale")
                        .param("quantity", "1000")
                        .param("level", "50")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("personalDashboard"));

        ExporterSale exporterSale = exporterSaleRepository.findAll().get(0);
        assertEquals(initialCount + 1, exporterSaleRepository.findAll().size());
        assertEquals(BigDecimal.valueOf(41400).setScale(2, RoundingMode.HALF_UP), exporterSale.getEmployeeAmount());
        assertEquals(BigDecimal.valueOf(12420).setScale(2, RoundingMode.HALF_UP), exporterSale.getCompanyAmount());
    }

    @Test
    @WithUserDetails("testUser")
    public void addExporterSaleWhenErrorInTheFormTest() throws Exception {
        int initialCount = exporterSaleRepository.findAll().size();

        mockMvc.perform(post("/addExporterSale")
                        .param("quantity", "1000")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addExporterSale"))
                .andExpect(model().attributeExists("exporterSale"));

        assertEquals(initialCount, exporterSaleRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void getAddCustomerSalePageTest() throws Exception {
        mockMvc.perform(get("/addCustomerSale"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addCustomerSale"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("typeOfSales"))
                .andExpect(model().attributeExists("contracts"));
    }

    @Test
    @WithUserDetails("testUser")
    public void addCustomerSaleTest() throws Exception {
        int initialCountCustomerSale = customerSaleRepository.findAll().size();
        int initialCountStock = stockRepository.findAll().size();
        int initialStock = productRepository.findAll().get(0).getStock();
        Product product = productRepository.findAll().get(0);
        Contract contract = contractRepository.findAll().get(0);

        mockMvc.perform(post("/addCustomerSale")
                        .param("product.id", product.getId().toString())
                        .param("product.name", product.getName())
                        .param("product.cleanMoney", product.getCleanMoney().toString())
                        .param("product.dirtyMoney", product.getDirtyMoney().toString())
                        .param("product.stock", product.getStock().toString())
                        .param("typeOfSale" , "cleanMoney")
                        .param("quantity", "1000")
                        .param("contract.id", contract.getId().toString())
                        .param("contract.company", contract.getCompany())
                        .param("contract.reduction", contract.getReduction().toString())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("personalDashboard"))
                .andExpect(model().attributeExists("personalDashboardDto"));

        CustomerSale customerSale = customerSaleRepository.findAll().get(0);
        assertEquals(initialCountCustomerSale + 1, customerSaleRepository.findAll().size());
        assertEquals(initialCountStock + 1, stockRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void addCustomerSaleDirtyMoneyTest() throws Exception {
        int initialCountCustomerSale = customerSaleRepository.findAll().size();
        int initialCountStock = stockRepository.findAll().size();
        int initialStock = productRepository.findAll().get(0).getStock();
        Product product = productRepository.findAll().get(0);

        mockMvc.perform(post("/addCustomerSale")
                        .param("product.id", product.getId().toString())
                        .param("product.name", product.getName())
                        .param("product.cleanMoney", product.getCleanMoney().toString())
                        .param("product.dirtyMoney", product.getDirtyMoney().toString())
                        .param("product.stock", product.getStock().toString())
                        .param("typeOfSale" , "dirtyMoney")
                        .param("quantity", "1000")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("personalDashboard"))
                .andExpect(model().attributeExists("personalDashboardDto"));

        CustomerSale customerSale = customerSaleRepository.findAll().get(0);
        assertEquals(initialCountCustomerSale + 1, customerSaleRepository.findAll().size());
        assertEquals(BigDecimal.valueOf(35000).setScale(2, RoundingMode.HALF_UP), customerSale.getAmount());
        assertEquals(initialStock - 1000, customerSale.getProduct().getStock());
        assertEquals(initialCountStock + 1, stockRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void addCustomerSaleWhenErrorInTheFormTest() throws Exception {
        int initialCountCustomerSale = customerSaleRepository.findAll().size();
        Product product = productRepository.findAll().get(0);

        mockMvc.perform(post("/addCustomerSale")
                        .param("product.id", product.getId().toString())
                        .param("product.name", product.getName())
                        .param("product.cleanMoney", product.getCleanMoney().toString())
                        .param("product.dirtyMoney", product.getDirtyMoney().toString())
                        .param("product.stock", product.getStock().toString())
                        .param("typeOfSale" , "cleanMoney")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addCustomerSale"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("typeOfSales"))
                .andExpect(model().attributeExists("contracts"));

        assertEquals(initialCountCustomerSale, customerSaleRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void getModifyStockPageTest() throws Exception {
        mockMvc.perform(get("/modifyStock"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("modifyStock"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("operationStocks"))
                .andExpect(model().attributeExists("stocks"));
    }

    @Test
    @WithUserDetails("testUser")
    public void modifyStockTest() throws Exception {
        int initialCount = stockRepository.findAll().size();
        Product product = productRepository.findAll().get(0);

        mockMvc.perform(post("/modifyStock")
                .param("product.id", product.getId().toString())
                .param("product.name", product.getName())
                .param("product.cleanMoney", product.getCleanMoney().toString())
                .param("product.dirtyMoney", product.getDirtyMoney().toString())
                .param("product.stock", product.getStock().toString())
                .param("quantity", "50")
                .param("operationStock", "add")
                .param("typeOfStockMovement", "stockModification")
                .with(csrf())
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("personalDashboard"))
                .andExpect(model().attributeExists("personalDashboardDto"));

        assertEquals(initialCount + 1, stockRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void modifyStockWhenErrorInTheFormTest() throws Exception {
        int initialCount = stockRepository.findAll().size();
        Product product = productRepository.findAll().get(0);

        mockMvc.perform(post("/modifyStock")
                        .param("product.id", product.getId().toString())
                        .param("product.name", product.getName())
                        .param("product.cleanMoney", product.getCleanMoney().toString())
                        .param("product.dirtyMoney", product.getDirtyMoney().toString())
                        .param("product.stock", product.getStock().toString())
                        .param("quantity", "50")
                        .param("typeOfStockMovement", "stockModification")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("modifyStock"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("operationStocks"))
                .andExpect(model().attributeExists("stocks"));


        assertEquals(initialCount, stockRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void getDashboardPageTest() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("dashboardDtoList"));
    }

    @Test
    @WithUserDetails("testUser")
    public void getPersonalDashboardPageTest() throws Exception {
        mockMvc.perform(get("/personalDashboard"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("personalDashboard"))
                .andExpect(model().attributeExists("personalDashboardDto"));
    }
}
