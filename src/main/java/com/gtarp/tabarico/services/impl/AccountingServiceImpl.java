package com.gtarp.tabarico.services.impl;

import com.gtarp.tabarico.dto.accouting.*;
import com.gtarp.tabarico.entities.User;
import com.gtarp.tabarico.entities.accounting.*;
import com.gtarp.tabarico.exception.CustomerDirtySaleRateNotFoundException;
import com.gtarp.tabarico.exception.UserNotFoundException;
import com.gtarp.tabarico.repositories.CustomerDirtySaleRateRepository;
import com.gtarp.tabarico.repositories.ProductRepository;
import com.gtarp.tabarico.repositories.UserRepository;
import com.gtarp.tabarico.repositories.accounting.CustomerSaleRepository;
import com.gtarp.tabarico.repositories.accounting.ExporterSaleRepository;
import com.gtarp.tabarico.repositories.accounting.StockRepository;
import com.gtarp.tabarico.services.AccountingService;
import com.gtarp.tabarico.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountingServiceImpl implements AccountingService {
    @Autowired
    private ExporterSaleRepository exporterSaleRepository;
    @Autowired
    private CustomerSaleRepository customerSaleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerDirtySaleRateRepository customerDirtySaleRateRepository;
    @Autowired
    private UserService userService;

    @Override
    public ExporterSale createExporterSale(ExporterSaleDto exporterSaleDto, String username) {
        ExporterSale exporterSale = new ExporterSale();
        exporterSale.setDate(LocalDateTime.now());

        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        exporterSale.setUser(user);
        exporterSale.setQuantity(exporterSaleDto.getQuantity());
        exporterSale.setLevel(exporterSaleDto.getLevel());
        exporterSale.setEmployeeAmount(calculateExporterEmployeeAmount(exporterSaleDto));
        //le montant employé * 0.3 pour obtenir le montant entreprise
        exporterSale.setCompanyAmount(exporterSale.getEmployeeAmount().multiply(BigDecimal.valueOf(0.3)).setScale(0, RoundingMode.HALF_UP));
        return exporterSaleRepository.save(exporterSale);
    }

    private BigDecimal calculateExporterEmployeeAmount(ExporterSaleDto exporterSaleDto) {
        int level = exporterSaleDto.getLevel();
        int quantity = exporterSaleDto.getQuantity();
        // si le level > 100, il n'y a plus de bonus sur le prix
        if (level > 100) {
            level = 100;
        }
        //36 est le prix d'une cigarette de base. on ajoute ensuite un bonus de 0.3% de 36 en fonction du niveau du vendeur et on multiplie par le nombre de cigarette
        return BigDecimal.valueOf((36+(36*(level*0.3/100)))*quantity).setScale(0, RoundingMode.HALF_UP);
    }

    @Override
    public CustomerSale createCustomerSale(CustomerSaleDto customerSaleDto, String username) {
        CustomerSale customerSale = new CustomerSale();
        customerSale.setDate(LocalDateTime.now());
        customerSale.setProduct(customerSaleDto.getProduct());
        customerSale.setTypeOfSale(customerSaleDto.getTypeOfSale());
        customerSale.setQuantity(customerSaleDto.getQuantity());
        customerSale.setContract(customerSaleDto.getContract());
        customerSale.setAmount(calculateCustomerSaleAmount(customerSaleDto));
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        customerSale.setUser(user);

        StockDto stockDto = new StockDto();
        stockDto.setProduct(customerSaleDto.getProduct());
        stockDto.setQuantity(customerSaleDto.getQuantity());
        stockDto.setOperationStock(OperationStock.remove);
        stockDto.setTypeOfStockMovement(TypeOfStockMovement.customerSale);
        modifyStock(stockDto, username);

        return customerSaleRepository.save(customerSale);
    }

    private BigDecimal calculateCustomerSaleAmount(CustomerSaleDto customerSaleDto) {
        BigDecimal pricePerUnit;
        if (customerSaleDto.getTypeOfSale().equals(TypeOfSale.cleanMoney)) {
            pricePerUnit = new BigDecimal(customerSaleDto.getProduct().getCleanMoney());
            //la reduction des contrats ne s'applique que sur les ventes en propre
            if(customerSaleDto.getContract() != null) {
                BigDecimal reduction = new BigDecimal(customerSaleDto.getContract().getReduction());
                pricePerUnit = pricePerUnit.subtract((pricePerUnit.multiply(reduction).divide(BigDecimal.valueOf(100))));
            }
        } else {
            pricePerUnit = new BigDecimal(customerSaleDto.getProduct().getDirtyMoney());
        }

        return pricePerUnit.multiply(BigDecimal.valueOf(customerSaleDto.getQuantity())).setScale(0, RoundingMode.HALF_UP);
    }

    public Stock modifyStock(StockDto stockDto, String username) {
        Stock stock = new Stock();
        stock.setDate(LocalDate.now());
        stock.setProduct(stockDto.getProduct());
        stock.setOperationStock(stockDto.getOperationStock());
        stock.setQuantity(stockDto.getQuantity());

        int newQuantity;
        if(stockDto.getOperationStock() != null && stockDto.getOperationStock().equals(OperationStock.add)) {
            newQuantity = stockDto.getProduct().getStock() + stockDto.getQuantity();
        } else {
            newQuantity = stockDto.getProduct().getStock() - stockDto.getQuantity();
        }
        stock.setStock(newQuantity);
        stockDto.getProduct().setStock(newQuantity);
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        stock.setUser(user);
        stock.setTypeOfStockMovement(stockDto.getTypeOfStockMovement());

        productRepository.save(stock.getProduct());
        return stockRepository.save(stock);
    }

    public List<Stock> getStockListByDate(LocalDate date) {
        return stockRepository.getStockListByDate(date);
    }

    public List<DashboardDto> getDashboardListOfThisWeek() {
        //On decoupe les semaines du dimanche au dimanche pour les semaines de compta
        LocalDate today = LocalDate.now();
        LocalDateTime startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).atTime(4, 0, 0, 0);
        LocalDateTime endOfWeek = startOfWeek.plusWeeks(1).withHour(3).withMinute(59).withSecond(59).withNano(999999999);

        List<DashboardDto> dashboardDtoList = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            user = userService.disableHolidayWhenExpire(user);
            DashboardDto dashboardDto = new DashboardDto();
            dashboardDto.setUser(user);

            //On recupere toutes les ventes client pour le user et on groupe par type de vente
            List<CustomerSale> customerSaleList = customerSaleRepository.findAllByUserAndDateBetween(user, startOfWeek, endOfWeek);
            Map<TypeOfSale, Integer> salesByType = getCustomerSalesByTypeOfSales(customerSaleList);
            dashboardDto.setCustomerSalesCleanMoney(salesByType.get(TypeOfSale.cleanMoney));
            dashboardDto.setCustomerSalesDirtyMoney(salesByType.get(TypeOfSale.dirtyMoney));

            //On fait la somme de toutes les ventes exportateurs
            List<ExporterSale> exporterSaleList = exporterSaleRepository.findAllByUserAndDateBetween(user, startOfWeek, endOfWeek);
            dashboardDto.setExporterSalesMoney(getExporterSalesMoney(exporterSaleList));
            dashboardDto.setExporterSalesQuantity(getExporterSalesQuantity(exporterSaleList));

            dashboardDto.setQuota(user.isQuota());
            dashboardDto.setExporterQuota(user.isExporterQuota());

            dashboardDto.setCleanMoneySalary(calculateCleanMoneySalary(dashboardDto.getExporterSalesMoney(), dashboardDto.getCustomerSalesCleanMoney(), user));
            
            dashboardDto.setDirtyMoneySalary(calculateDirtyMoneySalary(dashboardDto.getCustomerSalesDirtyMoney()));

            dashboardDto.setHoliday(user.isHoliday());
            dashboardDto.setEndOfHoliday(user.getEndOfHoliday());
            dashboardDto.setWarning1(user.isWarning1());
            dashboardDto.setWarning2(user.isWarning2());
            dashboardDto.setCleanMoneySalaryPreviousWeek(user.getCleanMoneySalaryPreviousWeek());
            dashboardDto.setDirtyMoneySalaryPreviousWeek(user.getDirtyMoneySalaryPreviousWeek());

            dashboardDtoList.add(dashboardDto);
        }
        return dashboardDtoList;
    }

    private Integer calculateDirtyMoneySalary(Integer customerSalesDirtyMoney) {
        //On calcule la prime en sale en fonction du taux de redistribution defini
        if (customerSalesDirtyMoney == null) {
            return 0;
        }
        int customerDirtySaleRate = customerDirtySaleRateRepository.findById(1).orElseThrow(() -> new CustomerDirtySaleRateNotFoundException(1)).getCustomerDirtySaleRate();
        return customerSalesDirtyMoney * customerDirtySaleRate / 100;
    }

    private static Map<TypeOfSale, Integer> getCustomerSalesByTypeOfSales(List<CustomerSale> customerSaleList) {
        return customerSaleList.stream()
                .collect(Collectors.groupingBy(CustomerSale::getTypeOfSale, Collectors.summingInt(customerSale -> customerSale.getAmount().intValueExact())));
    }

    private static int getExporterSalesQuantity(List<ExporterSale> exporterSaleList) {
        return exporterSaleList.stream()
                .mapToInt(ExporterSale::getQuantity)
                .sum();
    }

    private static int getExporterSalesMoney(List<ExporterSale> exporterSaleList) {
        return exporterSaleList.stream()
                .mapToInt(exporterSale -> exporterSale.getCompanyAmount().intValueExact())
                .sum();
    }

    private int calculateCleanMoneySalary(Integer exporterSalesMoney, Integer customerSalesCleanMoney, User user) {
        //Si le quota n'est pas effectué, la prime sera de 0
        if (user.isQuota() && user.isExporterQuota()) {
            if (exporterSalesMoney == null) {
                exporterSalesMoney = 0;
            }
            if (customerSalesCleanMoney == null) {
                customerSalesCleanMoney = 0;
            }
            return user.getRole().getSalary() + (exporterSalesMoney * user.getRole().getRedistributionRate() / 100) + (customerSalesCleanMoney * user.getRole().getRedistributionRate() / 100);
        }
        return 0;
    }
    
    @Scheduled(cron = "0 0 5 ? * SUN")
    public void resetAccounting(){
        //On va récupérer les infos de la semaine précédente pour calculer les salaires 
        LocalDate today = LocalDate.now();
        LocalDateTime endOfPreviousWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).atTime(3, 59, 59, 999999999);
        LocalDateTime startOfPreviousWeek = endOfPreviousWeek.minusWeeks(1).withHour(4).withMinute(0).withSecond(0).withNano(0);
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            List<ExporterSale> exporterSaleList = exporterSaleRepository.findAllByUserAndDateBetween(user, startOfPreviousWeek, endOfPreviousWeek);
            List<CustomerSale> customerSaleList = customerSaleRepository.findAllByUserAndDateBetween(user, startOfPreviousWeek, endOfPreviousWeek);
            Map<TypeOfSale, Integer> salesByType = getCustomerSalesByTypeOfSales(customerSaleList);
            user.setCleanMoneySalaryPreviousWeek(calculateCleanMoneySalary(getExporterSalesMoney(exporterSaleList), salesByType.get(TypeOfSale.cleanMoney), user));
            user.setDirtyMoneySalaryPreviousWeek(calculateDirtyMoneySalary(salesByType.get(TypeOfSale.dirtyMoney)));
            user.setQuota(false);
            user.setExporterQuota(false);
            userRepository.save(user);
        }
    }

    public PersonalDashboardDto getPersonalDashboardDto(String username) {
        //On decoupe les semaines du dimanche au dimanche pour les semaines de compta
        LocalDate today = LocalDate.now();
        LocalDateTime startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).atTime(4, 0, 0, 0);
        LocalDateTime endOfWeek = startOfWeek.plusWeeks(1).withHour(3).withMinute(59).withSecond(59).withNano(999999999);
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        List<ExporterSale> exporterSaleList = exporterSaleRepository.findAllByUserAndDateBetween(user, startOfWeek, endOfWeek);
        List<CustomerSale> customerSaleList = customerSaleRepository.findAllByUserAndDateBetween(user, startOfWeek, endOfWeek);
        Map<TypeOfSale, Integer> salesByType = getCustomerSalesByTypeOfSales(customerSaleList);
        PersonalDashboardDto personalDashboardDto = new PersonalDashboardDto();
        personalDashboardDto.setExporterSalesMoney(getExporterSalesMoney(exporterSaleList));
        personalDashboardDto.setExporterSalesQuantity(getExporterSalesQuantity(exporterSaleList));
        personalDashboardDto.setQuota(user.isQuota());
        personalDashboardDto.setExporterQuota(user.isExporterQuota());
        personalDashboardDto.setCleanMoneySalary(calculateCleanMoneySalary(personalDashboardDto.getExporterSalesMoney(), salesByType.get(TypeOfSale.cleanMoney), user));
        personalDashboardDto.setDirtyMoneySalary(calculateDirtyMoneySalary(salesByType.get(TypeOfSale.dirtyMoney)));
        personalDashboardDto.setHoliday(user.isHoliday());
        personalDashboardDto.setWarning1(user.isWarning1());
        personalDashboardDto.setWarning2(user.isWarning2());
        personalDashboardDto.setCleanMoneySalaryPreviousWeek(user.getCleanMoneySalaryPreviousWeek());
        personalDashboardDto.setDirtyMoneySalaryPreviousWeek(user.getCleanMoneySalaryPreviousWeek());
        personalDashboardDto.setExporterSaleList(exporterSaleList);
        personalDashboardDto.setCustomerSaleList(customerSaleList);
        return personalDashboardDto;
    }
}
