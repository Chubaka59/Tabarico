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
    @NotBlank
    private String company;
    @NotNull
    @Min(0)
    @Max(100)
    @Digits(integer = 3, fraction = 0)
    private Integer reduction;
}
