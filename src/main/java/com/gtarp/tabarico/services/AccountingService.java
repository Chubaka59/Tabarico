package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.accouting.AccountingSummaryDto;
import com.gtarp.tabarico.dto.accouting.CustomerSaleDto;
import com.gtarp.tabarico.dto.accouting.ExporterSaleDto;
import com.gtarp.tabarico.dto.accouting.StockDto;
import com.gtarp.tabarico.entities.accounting.CustomerSale;
import com.gtarp.tabarico.entities.accounting.ExporterSale;
import com.gtarp.tabarico.entities.accounting.Stock;

import java.time.LocalDate;
import java.util.List;

public interface AccountingService {
    ExporterSale createExporterSale(ExporterSaleDto exporterSaleDto, String username);
    CustomerSale createCustomerSale(CustomerSaleDto customerSaleDto, String username);
    Stock modifyStock(StockDto stockDto, String username);
    List<Stock> getStockListByDate(LocalDate date);
    List<AccountingSummaryDto> getAccountingSummaryListOfThisWeek();
    void resetAccounting();
}
