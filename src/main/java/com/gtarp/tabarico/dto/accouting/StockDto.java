package com.gtarp.tabarico.dto.accouting;

import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.entities.accounting.OperationStock;
import com.gtarp.tabarico.entities.accounting.TypeOfStockMovement;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {
    private Integer id;
    @NotNull(message = "Sélectionnez un produit")
    private Product product;
    @NotNull(message = "Indiquez une quantité")
    @Digits(integer = 10, fraction = 0)
    @Min(value = 1, message = "La vente minimum est de 1")
    private Integer quantity;
    @NotNull(message = "Sélectionnez une opération")
    private OperationStock operationStock;
    private TypeOfStockMovement typeOfStockMovement;
}
