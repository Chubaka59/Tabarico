package com.gtarp.tabarico.entities;

import com.gtarp.tabarico.dto.ContractDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String company;
    @NotNull
    @Min(0)
    @Max(100)
    @Digits(integer = 3, fraction = 0)
    private Integer reduction;

    public Contract(ContractDto contractDto) {
        this.company = contractDto.getCompany();
        this.reduction = contractDto.getReduction();
    }

    public Contract update(ContractDto contractDto) {
        this.company = contractDto.getCompany();
        this.reduction = contractDto.getReduction();
        return this;
    }
}
