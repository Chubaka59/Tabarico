package com.gtarp.tabarico.dto.accouting;

import com.gtarp.tabarico.entities.Contract;
import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.entities.accounting.TypeOfSale;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSaleDto {
    private Integer id;
    private Calendar date;
    private Product product;
    private TypeOfSale typeOfSale;
    @Digits(integer = 10, fraction = 0)
    @Min(1)
    private Integer quantity;
    private Contract contract;
}
