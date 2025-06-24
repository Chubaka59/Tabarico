package com.gtarp.tabarico.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private Integer id;
    @NotBlank
    private String name;
    @Digits(integer = 3, fraction = 0)
    @Min(0)
    @Max(100)
    private Integer redistributionRate;
    @Digits(integer = 10, fraction = 0)
    private Integer salary;
}
