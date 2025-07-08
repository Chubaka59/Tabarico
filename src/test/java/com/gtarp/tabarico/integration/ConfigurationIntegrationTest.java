package com.gtarp.tabarico.integration;


import com.gtarp.tabarico.entities.Contract;
import com.gtarp.tabarico.entities.CustomerDirtySaleRate;
import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.entities.Role;
import com.gtarp.tabarico.repositories.ContractRepository;
import com.gtarp.tabarico.repositories.CustomerDirtySaleRateRepository;
import com.gtarp.tabarico.repositories.ProductRepository;
import com.gtarp.tabarico.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ConfigurationIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomerDirtySaleRateRepository customerDirtySaleRateRepository;

    @Test
    @WithUserDetails("testUser")
    public void getConfigurationPageTest() throws Exception {
        mockMvc.perform(get("/configuration"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("configuration"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("contracts"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("customerDirtySaleRates"));
    }

    @Test
    @WithUserDetails("testUser")
    public void showAddProductPageTest() throws Exception {
        mockMvc.perform(get("/configuration/addProduct"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addProduct"));
    }

    @Test
    @WithUserDetails("testUser")
    public void addProductTest() throws Exception {
        int initialCount = productRepository.findAll().size();

        mockMvc.perform(post("/configuration/products")
                        .param("name", "test")
                        .param("cleanMoney", "100")
                        .param("dirtyMoney", "50")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("configuration"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("contracts"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("customerDirtySaleRates"));

        assertEquals(initialCount+1, productRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void addProductWhenErrorInTheFormTest() throws Exception {
        int initialCount = productRepository.findAll().size();

        mockMvc.perform(post("/configuration/products")
                        .param("name", "testProduct")
                        .param("cleanMoney", "100")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addProduct"));

        assertEquals(initialCount, productRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void addProductWhenProductAlreadyExistTest() throws Exception {
        int initialCount = productRepository.findAll().size();

        mockMvc.perform(post("/configuration/products")
                        .param("name", "testProduct")
                        .param("cleanMoney", "100")
                        .param("dirtyMoney", "50")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addProduct"));

        assertEquals(initialCount, productRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void deleteProductTest() throws Exception {
        int initialCount = productRepository.findAll().size();

        mockMvc.perform(get("/configuration/products/3/delete"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("configuration"));

        assertEquals(initialCount - 1, productRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void showUpdateProductPageTest() throws Exception {
        mockMvc.perform(get("/configuration/products/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateProduct"))
                .andExpect(model().attributeExists("productDto"));
    }

    @Test
    @WithUserDetails("testUser")
    public void updateProductTest() throws Exception {
        Product updatedInformationProduct = new Product(2, "testUpdatedProduct", 200, 100, 0);

        mockMvc.perform(post("/configuration/products/{id}", updatedInformationProduct.getId())
                        .param("name", updatedInformationProduct.getName())
                        .param("cleanMoney", updatedInformationProduct.getCleanMoney().toString())
                        .param("dirtyMoney", updatedInformationProduct.getDirtyMoney().toString())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("configuration"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("contracts"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("customerDirtySaleRates"));

        assertEquals(updatedInformationProduct, productRepository.findById(updatedInformationProduct.getId()).get());
    }

    @Test
    @WithUserDetails("testUser")
    public void updateProductWhenProductIsNotFoundTest() throws Exception {
        mockMvc.perform(post("/configuration/products/{id}", 99)
                        .param("name", "test")
                        .param("cleanMoney", "1")
                        .param("dirtyMoney", "1")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateProduct"));
    }

    @Test
    @WithUserDetails("testUser")
    public void updateProductWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/configuration/products/{id}", 2)
                        .param("name", "test")
                        .param("cleanMoney", "1")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateProduct"));
    }

    @Test
    @WithUserDetails("testUser")
    public void showAddContractPageTest() throws Exception {
        mockMvc.perform(get("/configuration/addContract"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addContract"));
    }

    @Test
    @WithUserDetails("testUser")
    public void addContractTest() throws Exception {
        int initialCount = contractRepository.findAll().size();

        mockMvc.perform(post("/configuration/contracts")
                        .param("company", "testCompanyToAdd")
                        .param("reduction", "10")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("configuration"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("contracts"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("customerDirtySaleRates"));

        assertEquals(initialCount+1, contractRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void addContractWhenErrorInTheFormTest() throws Exception {
        int initialCount = contractRepository.findAll().size();

        mockMvc.perform(post("/configuration/contracts")
                        .param("company", "testCompanyToAdd")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addContract"));

        assertEquals(initialCount, contractRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void addContractWhenProductAlreadyExistTest() throws Exception {
        int initialCount = contractRepository.findAll().size();

        mockMvc.perform(post("/configuration/contracts")
                        .param("company", "testCompany")
                        .param("reduction", "100")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addContract"));

        assertEquals(initialCount, contractRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void deleteContractTest() throws Exception {
        int initialCount = contractRepository.findAll().size();

        mockMvc.perform(get("/configuration/contracts/3/delete"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("configuration"));

        assertEquals(initialCount - 1, contractRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void showUpdateContractPageTest() throws Exception {
        mockMvc.perform(get("/configuration/contracts/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateContract"))
                .andExpect(model().attributeExists("contractDto"));
    }

    @Test
    @WithUserDetails("testUser")
    public void updateContractTest() throws Exception {
        Contract updatedInformationContract = new Contract(2, "testUpdatedCompany",20);

        mockMvc.perform(post("/configuration/contracts/{id}", updatedInformationContract.getId())
                        .param("company", updatedInformationContract.getCompany())
                        .param("reduction", updatedInformationContract.getReduction().toString())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("configuration"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("contracts"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("customerDirtySaleRates"));

        assertEquals(updatedInformationContract, contractRepository.findById(updatedInformationContract.getId()).get());
    }

    @Test
    @WithUserDetails("testUser")
    public void updateContractWhenContractIsNotFoundTest() throws Exception {
        mockMvc.perform(post("/configuration/contracts/{id}", 99)
                        .param("company", "test")
                        .param("reduction", "20")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateContract"));
    }

    @Test
    @WithUserDetails("testUser")
    public void updateContractWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/configuration/contracts/{id}", 2)
                        .param("company", "test")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateContract"));
    }

    @Test
    @WithUserDetails("testUser")
    public void showAddRolePageTest() throws Exception {
        mockMvc.perform(get("/configuration/addRole"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addRole"));
    }

    @Test
    @WithUserDetails("testUser")
    public void addRoleTest() throws Exception {
        int initialCount = roleRepository.findAll().size();

        mockMvc.perform(post("/configuration/roles")
                        .param("name", "testRoleToAdd")
                        .param("redistributionRate", "40")
                        .param("salary", "40000")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("configuration"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("contracts"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("customerDirtySaleRates"));

        assertEquals(initialCount+1, roleRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void addRoleWhenErrorInTheFormTest() throws Exception {
        int initialCount = roleRepository.findAll().size();

        mockMvc.perform(post("/configuration/roles")
                        .param("name", "testRoleToAdd")
                        .param("redistributionRate", "40")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addRole"));

        assertEquals(initialCount, roleRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void addRoleWhenProductAlreadyExistTest() throws Exception {
        int initialCount = roleRepository.findAll().size();

        mockMvc.perform(post("/configuration/contracts")
                        .param("company", "testCompany")
                        .param("reduction", "100")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addContract"));

        assertEquals(initialCount, roleRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void deleteRoleTest() throws Exception {
        int initialCount = roleRepository.findAll().size();

        mockMvc.perform(get("/configuration/roles/3/delete"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("configuration"));

        assertEquals(initialCount - 1, roleRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void showUpdateRolePageTest() throws Exception {
        mockMvc.perform(get("/configuration/roles/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateRole"))
                .andExpect(model().attributeExists("roleDto"));
    }

    @Test
    @WithUserDetails("testUser")
    public void updateRoleTest() throws Exception {
        Role updatedInformationRole = new Role(2, "testUpdatedRole",20, 20000);

        mockMvc.perform(post("/configuration/roles/{id}", updatedInformationRole.getId())
                        .param("name", updatedInformationRole.getName())
                        .param("redistributionRate", updatedInformationRole.getRedistributionRate().toString())
                        .param("salary", updatedInformationRole.getSalary().toString())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("configuration"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("contracts"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("customerDirtySaleRates"));

        assertEquals(updatedInformationRole, roleRepository.findById(updatedInformationRole.getId()).get());
    }

    @Test
    @WithUserDetails("testUser")
    public void updateRoleWhenRoleIsNotFoundTest() throws Exception {
        mockMvc.perform(post("/configuration/roles/{id}", 99)
                        .param("name", "test")
                        .param("redistributionRate", "20")
                        .param("salary", "20")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateRole"));
    }

    @Test
    @WithUserDetails("testUser")
    public void updateRoleWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/configuration/roles/{id}", 2)
                        .param("company", "test")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateRole"));
    }

    @Test
    @WithUserDetails("testUser")
    public void showUpdateCustomerDirtySaleRatePageTest() throws Exception {
        mockMvc.perform(get("/configuration/customerDirtySaleRates/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateCustomerDirtySaleRate"))
                .andExpect(model().attributeExists("customerDirtySaleRateDto"));
    }

    @Test
    @WithUserDetails("testUser")
    public void updateCustomerDirtySaleRateTest() throws Exception {
        CustomerDirtySaleRate updatedInformationCustomerDirtySaleRate = new CustomerDirtySaleRate(1, 10);

        mockMvc.perform(post("/configuration/customerDirtySaleRates/{id}", updatedInformationCustomerDirtySaleRate.getId())
                        .param("customerDirtySaleRate", updatedInformationCustomerDirtySaleRate.getCustomerDirtySaleRate().toString())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("configuration"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("contracts"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("customerDirtySaleRates"));

        assertEquals(updatedInformationCustomerDirtySaleRate, customerDirtySaleRateRepository.findById(updatedInformationCustomerDirtySaleRate.getId()).get());
    }

    @Test
    @WithUserDetails("testUser")
    public void updateCustomerDirtySaleRateWhenCustomerDirtySaleRateIsNotFoundTest() throws Exception {
        mockMvc.perform(post("/configuration/customerDirtySaleRates/{id}", 99)
                        .param("customerDirtySaleRate", "10")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateCustomerDirtySaleRate"));
    }

    @Test
    @WithUserDetails("testUser")
    public void updateCustomerDirtySaleRateWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/configuration/customerDirtySaleRates/{id}", 1)
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateCustomerDirtySaleRate"));
    }
}
