package com.gtarp.tabarico.dto.accouting;

import com.gtarp.tabarico.entities.User;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExporterSaleDto {
    private Integer id;
    private User user;
    @Digits(integer = 10, fraction = 0)
    private Integer quantity;
    private Integer level;
}
