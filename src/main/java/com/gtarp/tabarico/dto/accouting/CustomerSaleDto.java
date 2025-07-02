package com.gtarp.tabarico.dto.accouting;

import com.gtarp.tabarico.entities.Contract;
import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.entities.accounting.TypeOfSale;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSaleDto {
    private Integer id;
    @NotNull(message = "Sélectionnez un produit")
    private Product product;
    @NotNull(message = "Sélectionnez un type de vente")
    private TypeOfSale typeOfSale;
    @NotNull(message = "Indiquez une quantité")
    @Digits(integer = 10, fraction = 0)
    @Min(value = 1, message = "La quantité minimum est de 1")
    private Integer quantity;
    private Contract contract;
}
