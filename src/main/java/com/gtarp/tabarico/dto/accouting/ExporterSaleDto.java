package com.gtarp.tabarico.dto.accouting;

import com.gtarp.tabarico.entities.User;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExporterSaleDto {
    private Integer id;
    private User user;
    @NotNull(message = "Indiquez une quantité")
    @Min(value = 1, message = "La vente minimum est de 1")
    @Digits(integer = 10, fraction = 0)
    private Integer quantity;
    @NotNull(message = "Indiquez votre niveau")
    @Min(value = 1, message = "Le niveau minimum est 1")
    @Digits(integer = 10, fraction = 0)
    private Integer level;
}
