package com.gtarp.tabarico.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Integer id;
    @NotBlank(message = "Indiquez un nom de produit")
    private String name;
    @NotNull(message = "Indiquez un tarif")
    @Min(value = 1, message = "le tarif doit être supérieur a 0")
    @Digits(integer = 3, fraction = 0, message = "Le tarif doit être un nombre entier")
    private Integer cleanMoney;
    @NotNull(message = "Indiquez un tarif")
    @Min(value = 1, message = "le tarif doit être supérieur a 0")
    @Digits(integer = 3, fraction = 0, message = "Le tarif doit être un nombre entier")
    private Integer dirtyMoney;
}
