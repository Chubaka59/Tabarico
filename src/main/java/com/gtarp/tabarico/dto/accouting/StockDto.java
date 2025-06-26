package com.gtarp.tabarico.dto.accouting;

import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.entities.accounting.TypeOfStockMovement;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {
    private Integer id;
    private Product product;
    @Digits(integer = 10, fraction = 0)
    private Integer quantity;
    private OperationStock operationStock;
    private TypeOfStockMovement typeOfStockMovement;
}
