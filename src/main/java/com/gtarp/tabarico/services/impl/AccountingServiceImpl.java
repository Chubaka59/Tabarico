package com.gtarp.tabarico.services.impl;

import com.gtarp.tabarico.dto.accouting.CustomerSaleDto;
import com.gtarp.tabarico.dto.accouting.ExporterSaleDto;
import com.gtarp.tabarico.entities.User;
import com.gtarp.tabarico.entities.accounting.CustomerSale;
import com.gtarp.tabarico.entities.accounting.ExporterSale;
import com.gtarp.tabarico.entities.accounting.TypeOfSale;
import com.gtarp.tabarico.exception.UserNotFoundException;
import com.gtarp.tabarico.repositories.UserRepository;
import com.gtarp.tabarico.repositories.accounting.CustomerSaleRepository;
import com.gtarp.tabarico.repositories.accounting.ExporterSaleRepository;
import com.gtarp.tabarico.services.AccountingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.Calendar;

@Service
public class AccountingServiceImpl implements AccountingService {
    @Autowired
    private ExporterSaleRepository exporterSaleRepository;
    @Autowired
    private CustomerSaleRepository customerSaleRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ExporterSale createExporterSale(ExporterSaleDto exporterSaleDto, Principal principal) {
        ExporterSale exporterSale = new ExporterSale();
        exporterSale.setDate(Calendar.getInstance());

        User user = userRepository.findUserByUsername(principal.getName()).orElseThrow(() -> new UserNotFoundException(principal.getName()));
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
    public CustomerSale createCustomerSale(CustomerSaleDto customerSaleDto, Principal principal) {
        CustomerSale customerSale = new CustomerSale();
        customerSale.setDate(Calendar.getInstance());
        customerSale.setProduct(customerSaleDto.getProduct());
        customerSale.setTypeOfSale(customerSaleDto.getTypeOfSale());
        customerSale.setQuantity(customerSaleDto.getQuantity());
        customerSale.setContract(customerSaleDto.getContract());
        customerSale.setAmount(calculateCustomerSaleAmount(customerSaleDto));
        User user = userRepository.findUserByUsername(principal.getName()).orElseThrow(() -> new UserNotFoundException(principal.getName()));
        customerSale.setUser(user);

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
}
