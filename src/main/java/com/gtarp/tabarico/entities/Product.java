package com.gtarp.tabarico.entities;

import com.gtarp.tabarico.dto.ProductDto;
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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private Integer cleanMoney;
    @NotBlank
    private Integer dirtyMoney;

    public Product(ProductDto productDto) {
        this.name = productDto.getName();
        this.cleanMoney = productDto.getCleanMoney();
        this.dirtyMoney = productDto.getDirtyMoney();
    }

    public Product update(ProductDto productDto) {
        this.name = productDto.getName();
        this.cleanMoney = productDto.getCleanMoney();
        this.dirtyMoney = productDto.getDirtyMoney();
        return this;
    }
}
