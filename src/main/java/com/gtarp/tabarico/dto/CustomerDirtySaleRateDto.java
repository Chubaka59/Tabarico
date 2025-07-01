package com.gtarp.tabarico.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDirtySaleRateDto {
    private Integer id;
    @Digits(integer = 3, fraction = 0)
    @Min(0)
    @Max(100)
    private Integer CustomerDirtySaleRate;
}
