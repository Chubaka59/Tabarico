package com.gtarp.tabarico.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private Integer reduction;
}
