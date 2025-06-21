package com.gtarp.tabarico.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private Integer cleanMoney;
    @NotBlank
    private Integer dirtyMoney;
}
