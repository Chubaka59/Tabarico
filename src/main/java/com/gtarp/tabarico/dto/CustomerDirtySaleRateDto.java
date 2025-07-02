package com.gtarp.tabarico.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDirtySaleRateDto {
    private Integer id;
    @NotNull(message = "Indiquez un % de réduction")
    @Digits(integer = 3, fraction = 0)
    @Min(value = 1, message = "La réduction doit être supérieure a 0")
    @Max(value = 100, message = "La réduction doit être inférieure ou égale a 100")
    private Integer customerDirtySaleRate;
}
