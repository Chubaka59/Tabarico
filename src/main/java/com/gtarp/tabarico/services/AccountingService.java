package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.accouting.CustomerSaleDto;
import com.gtarp.tabarico.dto.accouting.ExporterSaleDto;
import com.gtarp.tabarico.entities.accounting.CustomerSale;
import com.gtarp.tabarico.entities.accounting.ExporterSale;

import java.security.Principal;

public interface AccountingService {
    ExporterSale createExporterSale(ExporterSaleDto exporterSaleDto, Principal principal);
    CustomerSale createCustomerSale(CustomerSaleDto customerSaleDto, Principal principal);
}
