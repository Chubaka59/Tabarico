package com.gtarp.tabarico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayModificationDto {
    private Integer userId;
    private boolean newValue;
    private LocalDate endOfHoliday;
}
