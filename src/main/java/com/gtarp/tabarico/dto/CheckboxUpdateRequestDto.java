package com.gtarp.tabarico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckboxUpdateRequestDto {
    private Integer userId;
    private String fieldName;
    private boolean newValue;
}
