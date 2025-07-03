package com.gtarp.tabarico.dto.accouting;

import com.gtarp.tabarico.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {
    private User user;
    private Integer customerSalesCleanMoney;
    private Integer customerSalesDirtyMoney;
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
}
