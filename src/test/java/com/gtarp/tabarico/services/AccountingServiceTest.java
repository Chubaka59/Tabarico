package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.accouting.*;
import com.gtarp.tabarico.entities.*;
import com.gtarp.tabarico.entities.accounting.*;
import com.gtarp.tabarico.repositories.CustomerDirtySaleRateRepository;
import com.gtarp.tabarico.repositories.ProductRepository;
import com.gtarp.tabarico.repositories.UserRepository;
import com.gtarp.tabarico.repositories.accounting.CustomerSaleRepository;
import com.gtarp.tabarico.repositories.accounting.ExporterSaleRepository;
import com.gtarp.tabarico.repositories.accounting.StockRepository;
import com.gtarp.tabarico.services.impl.AccountingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AccountingServiceTest {
    @InjectMocks
    private AccountingServiceImpl accountingService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ExporterSaleRepository exporterSaleRepository;
    @Mock
    private CustomerSaleRepository customerSaleRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private CustomerDirtySaleRateRepository customerDirtySaleRateRepository;
    @Mock
    private UserService userService;

    @Test
    public void createExporterSaleTest() {
        //GIVEN a user should be found and an exportSale should be saved
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(exporterSaleRepository.save(any(ExporterSale.class))).thenReturn(new ExporterSale());
        ExporterSaleDto exporterSaleDto = new ExporterSaleDto();
        exporterSaleDto.setUser(new User());
        exporterSaleDto.setLevel(10);
        exporterSaleDto.setQuantity(100);

        //WHEN we create the exportersale
        ExporterSale actualExporterSale = accountingService.createExporterSale(exporterSaleDto, "testUsername");

        //THEN the exporterSale is saved with the correct data
        verify(exporterSaleRepository).save(any(ExporterSale.class));
        verify(userRepository).findUserByUsername(anyString());
    }

    @Test
    public void createCustomerSaleTest() {
        //GIVEN a user should be found and an CustomerSale should be saved
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(customerSaleRepository.save(any(CustomerSale.class))).thenReturn(new CustomerSale());
        when(productRepository.save(any(Product.class))).thenReturn(new Product());
        when(stockRepository.save(any(Stock.class))).thenReturn(new Stock());
        Product product = new Product(1, "testProduct", 100, 50, 1000);
        CustomerSaleDto customerSaleDto = new CustomerSaleDto(1, product, TypeOfSale.dirtyMoney, 100, new Contract());

        //WHEN we create the customer Sale
        CustomerSale customerSale = accountingService.createCustomerSale(customerSaleDto, "testUsername");

        //THEN the user is found and the customerSale is saved
        verify(customerSaleRepository, times(1)).save(any(CustomerSale.class));
        verify(userRepository, times(2)).findUserByUsername(anyString());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    public void modifyStockTest() {
        //GIVEN this should save a stock and a product
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(productRepository.save(any(Product.class))).thenReturn(new Product());
        when(stockRepository.save(any(Stock.class))).thenReturn(new Stock());
        Product product = new Product(1, "testProduct", 100, 50, 1000);
        StockDto stockDto = new StockDto(1, product, 100, OperationStock.remove, TypeOfStockMovement.stockModification);

        //WHEN we try to modify the stock
        Stock stock = accountingService.modifyStock(stockDto, "testUsername");

        //THEN we the product and the stock are saved
        verify(stockRepository, times(1)).save(any(Stock.class));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void getStockListByDateTest() {
        //GIVEN we should get a list of stock
        when(stockRepository.getStockListByDate(any(LocalDate.class))).thenReturn(new ArrayList<>());

        //WHEN we try to get the stock list
        List<Stock> stockList = accountingService.getStockListByDate(LocalDate.now());

        //THEN we get the stock list
        verify(stockRepository, times(1)).getStockListByDate(any(LocalDate.class));
    }

    @Test
    public void getDashboardListOfThisWeekWhenQuotaIsFalseTest() {
        //GIVEN we should get a list of users, a list of customerSale, a list of exporterSale, a customerDirtySaleRate
        Role role = new Role(1, "testRole", 40, 30000);
        User user = new User(1, "testUsername", "testPassword", "testLastName", "testFirstName", "testPhone", false, role, false, null, false, false, 30000, 10000, false, false);
        when(userRepository.findAll()).thenReturn(List.of(user));
        Product product = new Product(1, "testProduct", 100, 50, 1000);
        CustomerSale customerSale1 = new CustomerSale(1, LocalDateTime.now(), product, TypeOfSale.cleanMoney, 100, null, BigDecimal.valueOf(7000), user);
        CustomerSale customerSale2 = new CustomerSale(2, LocalDateTime.now(), product, TypeOfSale.dirtyMoney, 100, null, BigDecimal.valueOf(3500), user);
        CustomerSale customerSale3 = new CustomerSale(3, LocalDateTime.now(), product, TypeOfSale.cleanMoney, 100, null, BigDecimal.valueOf(7000), user);
        when(customerSaleRepository.findAllByUserAndDateBetween(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(customerSale1, customerSale2, customerSale3));
        ExporterSale exporterSale1 = new ExporterSale(1, LocalDateTime.now(), user, 1000, 50, BigDecimal.valueOf(100000), BigDecimal.valueOf(10000));
        ExporterSale exporterSale2 = new ExporterSale(2, LocalDateTime.now(), user, 500, 10, BigDecimal.valueOf(50000), BigDecimal.valueOf(5000));
        when(exporterSaleRepository.findAllByUserAndDateBetween(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(exporterSale1, exporterSale2));
        CustomerDirtySaleRate customerDirtySaleRate = new CustomerDirtySaleRate(1, 35);
        when(customerDirtySaleRateRepository.findById(anyInt())).thenReturn(Optional.of(customerDirtySaleRate));
        when(userService.disableHolidayWhenExpire(any(User.class))).thenReturn(user);


        //WHEN we call the method to get the list
        List<DashboardDto> dashboardDtoList = accountingService.getDashboardListOfThisWeek();

        //THEN we get the correct Data
        assertEquals(1, dashboardDtoList.size());
        assertEquals(user, dashboardDtoList.get(0).getUser());
        assertEquals(14000, dashboardDtoList.get(0).getCustomerSalesCleanMoney());
        assertEquals(3500, dashboardDtoList.get(0).getCustomerSalesDirtyMoney());
        assertEquals(15000, dashboardDtoList.get(0).getExporterSalesMoney());
        assertEquals(1500, dashboardDtoList.get(0).getExporterSalesQuantity());
        assertFalse(dashboardDtoList.get(0).isQuota());
        assertFalse(dashboardDtoList.get(0).isExporterQuota());
        //0 for salary has quota isn't checked
        assertEquals(0, dashboardDtoList.get(0).getCleanMoneySalary());
        assertEquals(3500 * customerDirtySaleRate.getCustomerDirtySaleRate() / 100, dashboardDtoList.get(0).getDirtyMoneySalary());
        assertFalse(dashboardDtoList.get(0).isHoliday());
        assertFalse(dashboardDtoList.get(0).isWarning1());
        assertFalse(dashboardDtoList.get(0).isWarning2());
    }

    @Test
    public void getDashboardListOfThisWeekWhenQuotaIsTrueTest() {
        //GIVEN we should get a list of users, a list of customerSale, a list of exporterSale, a customerDirtySaleRate
        Role role = new Role(1, "testRole", 40, 30000);
        User user = new User(1, "testUsername", "testPassword", "testLastName", "testFirstName", "testPhone", true, role, true, null, true, true, 30000, 10000, true, true);
        when(userRepository.findAll()).thenReturn(List.of(user));
        Product product = new Product(1, "testProduct", 100, 50, 1000);
        CustomerSale customerSale1 = new CustomerSale(1, LocalDateTime.now(), product, TypeOfSale.cleanMoney, 100, null, BigDecimal.valueOf(7000), user);
        CustomerSale customerSale2 = new CustomerSale(2, LocalDateTime.now(), product, TypeOfSale.dirtyMoney, 100, null, BigDecimal.valueOf(3500), user);
        CustomerSale customerSale3 = new CustomerSale(3, LocalDateTime.now(), product, TypeOfSale.cleanMoney, 100, null, BigDecimal.valueOf(7000), user);
        when(customerSaleRepository.findAllByUserAndDateBetween(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(customerSale1, customerSale2, customerSale3));
        ExporterSale exporterSale1 = new ExporterSale(1, LocalDateTime.now(), user, 1000, 50, BigDecimal.valueOf(100000), BigDecimal.valueOf(10000));
        ExporterSale exporterSale2 = new ExporterSale(2, LocalDateTime.now(), user, 500, 10, BigDecimal.valueOf(50000), BigDecimal.valueOf(5000));
        when(exporterSaleRepository.findAllByUserAndDateBetween(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(exporterSale1, exporterSale2));
        CustomerDirtySaleRate customerDirtySaleRate = new CustomerDirtySaleRate(1, 35);
        when(customerDirtySaleRateRepository.findById(anyInt())).thenReturn(Optional.of(customerDirtySaleRate));
        when(userService.disableHolidayWhenExpire(any(User.class))).thenReturn(user);


        //WHEN we call the method to get the list
        List<DashboardDto> dashboardDtoList = accountingService.getDashboardListOfThisWeek();

        //THEN we get the correct Data
        assertEquals(1, dashboardDtoList.size());
        assertEquals(user, dashboardDtoList.get(0).getUser());
        assertEquals(14000, dashboardDtoList.get(0).getCustomerSalesCleanMoney());
        assertEquals(3500, dashboardDtoList.get(0).getCustomerSalesDirtyMoney());
        assertEquals(15000, dashboardDtoList.get(0).getExporterSalesMoney());
        assertEquals(1500, dashboardDtoList.get(0).getExporterSalesQuantity());
        assertTrue(dashboardDtoList.get(0).isQuota());
        assertTrue(dashboardDtoList.get(0).isExporterQuota());
        //0 for salary has quota isn't checked
        assertEquals(41600, dashboardDtoList.get(0).getCleanMoneySalary());
        assertEquals(3500 * customerDirtySaleRate.getCustomerDirtySaleRate() / 100, dashboardDtoList.get(0).getDirtyMoneySalary());
        assertTrue(dashboardDtoList.get(0).isHoliday());
        assertTrue(dashboardDtoList.get(0).isWarning1());
        assertTrue(dashboardDtoList.get(0).isWarning2());
    }
    
    @Test
    public void resetAccountingTest() {
        //GIVEN we should get a list of user, a list of exporterSale, a list of customerSale and save a user
        Role role = new Role(1, "testRole", 40, 30000);
        User user = new User(1, "testUsername", "testPassword", "testLastName", "testFirstName", "testPhone", false, role, false, null, false, false, 30000, 10000, false, false);
        when(userRepository.findAll()).thenReturn(List.of(user));
        Product product = new Product(1, "testProduct", 100, 50, 1000);
        CustomerSale customerSale1 = new CustomerSale(1, LocalDateTime.now(), product, TypeOfSale.cleanMoney, 100, null, BigDecimal.valueOf(7000), user);
        CustomerSale customerSale2 = new CustomerSale(2, LocalDateTime.now(), product, TypeOfSale.dirtyMoney, 100, null, BigDecimal.valueOf(3500), user);
        CustomerSale customerSale3 = new CustomerSale(3, LocalDateTime.now(), product, TypeOfSale.cleanMoney, 100, null, BigDecimal.valueOf(7000), user);
        when(customerSaleRepository.findAllByUserAndDateBetween(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(customerSale1, customerSale2, customerSale3));
        ExporterSale exporterSale1 = new ExporterSale(1, LocalDateTime.now(), user, 1000, 50, BigDecimal.valueOf(100000), BigDecimal.valueOf(10000));
        ExporterSale exporterSale2 = new ExporterSale(2, LocalDateTime.now(), user, 500, 10, BigDecimal.valueOf(50000), BigDecimal.valueOf(5000));
        when(exporterSaleRepository.findAllByUserAndDateBetween(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(exporterSale1, exporterSale2));
        CustomerDirtySaleRate customerDirtySaleRate = new CustomerDirtySaleRate(1, 35);
        when(customerDirtySaleRateRepository.findById(anyInt())).thenReturn(Optional.of(customerDirtySaleRate));
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        //WHEN we try to reset the accouting
        accountingService.resetAccounting();
        
        //THEN the user has been updated
        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository,times(1)).findAll();
        verify(exporterSaleRepository, times(1)).findAllByUserAndDateBetween(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(customerSaleRepository, times(1)).findAllByUserAndDateBetween(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(customerDirtySaleRateRepository, times(1)).findById(anyInt());
    }

    @Test
    public void getPersonalDashboardDtoTest() {
        //GIVEN we should get a list of user, a list of exporterSale, a list of customerSale and save a user
        Role role = new Role(1, "testRole", 40, 30000);
        User user = new User(1, "testUsername", "testPassword", "testLastName", "testFirstName", "testPhone", false, role, false, null, false, false, 30000, 10000, true, true);
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));
        Product product = new Product(1, "testProduct", 100, 50, 1000);
        CustomerSale customerSale1 = new CustomerSale(1, LocalDateTime.now(), product, TypeOfSale.cleanMoney, 100, null, BigDecimal.valueOf(7000), user);
        CustomerSale customerSale2 = new CustomerSale(2, LocalDateTime.now(), product, TypeOfSale.dirtyMoney, 100, null, BigDecimal.valueOf(3500), user);
        CustomerSale customerSale3 = new CustomerSale(3, LocalDateTime.now(), product, TypeOfSale.cleanMoney, 100, null, BigDecimal.valueOf(7000), user);
        when(customerSaleRepository.findAllByUserAndDateBetween(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(customerSale1, customerSale2, customerSale3));
        ExporterSale exporterSale1 = new ExporterSale(1, LocalDateTime.now(), user, 1000, 50, BigDecimal.valueOf(100000), BigDecimal.valueOf(10000));
        ExporterSale exporterSale2 = new ExporterSale(2, LocalDateTime.now(), user, 500, 10, BigDecimal.valueOf(50000), BigDecimal.valueOf(5000));
        when(exporterSaleRepository.findAllByUserAndDateBetween(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(exporterSale1, exporterSale2));
        CustomerDirtySaleRate customerDirtySaleRate = new CustomerDirtySaleRate(1, 35);
        when(customerDirtySaleRateRepository.findById(anyInt())).thenReturn(Optional.of(customerDirtySaleRate));

        //WHEN we get the data
        PersonalDashboardDto personalDashboardDto = accountingService.getPersonalDashboardDto("test");

        //THEN we get the correct data
        verify(userRepository,times(1)).findUserByUsername(anyString());
        verify(exporterSaleRepository, times(1)).findAllByUserAndDateBetween(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(customerSaleRepository, times(1)).findAllByUserAndDateBetween(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(customerDirtySaleRateRepository, times(1)).findById(anyInt());
        assertEquals(15000, personalDashboardDto.getExporterSalesMoney());
        assertEquals(1500, personalDashboardDto.getExporterSalesQuantity());
        assertEquals(41600, personalDashboardDto.getCleanMoneySalary());
        assertEquals(1225, personalDashboardDto.getDirtyMoneySalary());
    }
}
