package com.gtarp.tabarico.entities;

import com.gtarp.tabarico.dto.ContractDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
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
