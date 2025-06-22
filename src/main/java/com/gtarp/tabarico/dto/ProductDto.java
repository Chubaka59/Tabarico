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
    @NotBlank
    private String name;
    @NotNull
    @Min(1)
    @Digits(integer = 3, fraction = 0)
    private Integer cleanMoney;
    @NotNull
    @Min(1)
    @Digits(integer = 3, fraction = 0)
    private Integer dirtyMoney;
}
