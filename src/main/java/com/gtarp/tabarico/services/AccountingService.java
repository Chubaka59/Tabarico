package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.accouting.CustomerSaleDto;
import com.gtarp.tabarico.dto.accouting.ExporterSaleDto;
import com.gtarp.tabarico.dto.accouting.StockDto;
import com.gtarp.tabarico.entities.accounting.CustomerSale;
import com.gtarp.tabarico.entities.accounting.ExporterSale;
import com.gtarp.tabarico.entities.accounting.Stock;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

public interface AccountingService {
    ExporterSale createExporterSale(ExporterSaleDto exporterSaleDto, Principal principal);
    CustomerSale createCustomerSale(CustomerSaleDto customerSaleDto, Principal principal);
    Stock modifyStock(StockDto stockDto, Principal principal);
    List<Stock> getStockListByDate(LocalDate date);
}
