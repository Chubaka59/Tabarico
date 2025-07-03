package com.gtarp.tabarico.dto.accouting;

import com.gtarp.tabarico.entities.accounting.CustomerSale;
import com.gtarp.tabarico.entities.accounting.ExporterSale;
import lombok.Data;

import java.util.List;

@Data
public class PersonalDashboardDto {
    private Integer exporterSalesMoney;
    private Integer exporterSalesQuantity;
    private boolean quota;
    private boolean exporterQuota;
    private Integer cleanMoneySalary;
    private Integer dirtyMoneySalary;
    private boolean holiday;
    private boolean warning1;
    private boolean warning2;
    private Integer cleanMoneySalaryPreviousWeek;
    private Integer dirtyMoneySalaryPreviousWeek;
    private List<ExporterSale> exporterSaleList;
    private List<CustomerSale> customerSaleList;
}
