package com.gtarp.tabarico.services.impl;

import com.gtarp.tabarico.dto.accouting.AccountingSummaryDto;
import com.gtarp.tabarico.dto.accouting.CustomerSaleDto;
import com.gtarp.tabarico.dto.accouting.ExporterSaleDto;
import com.gtarp.tabarico.dto.accouting.StockDto;
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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
        int pricePerUnit;
        if (customerSaleDto.getTypeOfSale().equals(TypeOfSale.cleanMoney)) {
            pricePerUnit = customerSaleDto.getProduct().getCleanMoney();
            //la reduction des contrats ne s'applique que sur les ventes en propre
            if(customerSaleDto.getContract() != null) {
                pricePerUnit = pricePerUnit - (pricePerUnit * customerSaleDto.getContract().getReduction() / 100);
            }
        } else {
            pricePerUnit = customerSaleDto.getProduct().getDirtyMoney();
        }

        return BigDecimal.valueOf(pricePerUnit*customerSaleDto.getQuantity()).setScale(0, RoundingMode.HALF_UP);
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

    public List<AccountingSummaryDto> getAccountingSummaryListOfThisWeek() {
        //On decoupe les semaines du dimanche au dimanche pour les semaines de compta
        LocalDate today = LocalDate.now();
        LocalDateTime startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).atTime(22, 0, 0, 0);
        LocalDateTime endOfWeek = startOfWeek.plusWeeks(1).withHour(21).withMinute(59).withSecond(59).withNano(999999999);;

        List<AccountingSummaryDto> accountingSummaryDtoList = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            AccountingSummaryDto accountingSummaryDto = new AccountingSummaryDto();
            accountingSummaryDto.setUser(user);

            //On recupere toutes les ventes client pour le user et on groupe par type de vente
            Map<TypeOfSale, Integer> salesByType = customerSaleRepository.findAllByUserAndDateBetween(user, startOfWeek, endOfWeek).stream()
                    .collect(Collectors.groupingBy(CustomerSale::getTypeOfSale, Collectors.summingInt(customerSale -> customerSale.getAmount().intValueExact())));
            accountingSummaryDto.setCustomerSalesCleanMoney(salesByType.get(TypeOfSale.cleanMoney));
            accountingSummaryDto.setCustomerSalesDirtyMoney(salesByType.get(TypeOfSale.dirtyMoney));

            //On fait la somme de toutes les ventes exportateurs
            List<ExporterSale> exporterSaleList = exporterSaleRepository.findAllByUserAndDateBetween(user, startOfWeek, endOfWeek);
            accountingSummaryDto.setExporterSalesMoney(exporterSaleList.stream()
                    .mapToInt(exporterSale -> exporterSale.getCompanyAmount().intValueExact())
                    .sum());
            accountingSummaryDto.setExporterSalesQuantity(exporterSaleList.stream()
                    .mapToInt(ExporterSale::getQuantity)
                    .sum());

            accountingSummaryDto.setQuota(user.isQuota());
            accountingSummaryDto.setExporterQuota(user.isExporterQuota());

            accountingSummaryDto.setCleanMoneySalary(calculateCleanMoneySalary(accountingSummaryDto.getExporterSalesMoney() != null ? accountingSummaryDto.getExporterSalesMoney() : 0, accountingSummaryDto.getCustomerSalesCleanMoney() != null ? accountingSummaryDto.getCustomerSalesCleanMoney() : 0, user));

            //On calcule la prime en sale en fonction du taux de redistribution defini
            int customerDirtySaleRate = customerDirtySaleRateRepository.findById(1).orElseThrow(() -> new CustomerDirtySaleRateNotFoundException(1)).getCustomerDirtySaleRate();
            accountingSummaryDto.setDirtyMoneySalary((accountingSummaryDto.getCustomerSalesDirtyMoney() != null ? accountingSummaryDto.getCustomerSalesDirtyMoney() : 0) * customerDirtySaleRate / 100);

            accountingSummaryDto.setHoliday(user.isHoliday());
            accountingSummaryDto.setWarning1(user.isWarning1());
            accountingSummaryDto.setWarning2(user.isWarning2());
            accountingSummaryDto.setCleanMoneySalaryPreviousWeek(user.getCleanMoneySalaryPreviousWeek());
            accountingSummaryDto.setDirtyMoneySalaryPreviousWeek(user.getDirtyMoneySalaryPreviousWeek());

            accountingSummaryDtoList.add(accountingSummaryDto);
        }
        return accountingSummaryDtoList;
    }

    private int calculateCleanMoneySalary(int exporterSalesMoney, int customerSalesCleanMoney, User user) {
        //Si le quota n'est pas effectué, la prime sera de 0
        if (user.isQuota() && user.isExporterQuota()) {
            return user.getRole().getSalary() + (exporterSalesMoney * user.getRole().getRedistributionRate() / 100) + (customerSalesCleanMoney * user.getRole().getRedistributionRate() / 100);
        }
        return 0;
    }
}
