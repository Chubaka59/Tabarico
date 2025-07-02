package com.gtarp.tabarico.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractDto {
    private Integer id;
    @NotBlank(message = "Indiquez un nom")
    private String company;
    @NotNull(message = "Indiquez un % de réduction")
    @Min(value = 1, message = "La réduction doit être supérieure a 0")
    @Max(value = 100, message = "La réduction doit être inférieure ou égale a 100")
    @Digits(integer = 3, fraction = 0)
    private Integer reduction;
}
