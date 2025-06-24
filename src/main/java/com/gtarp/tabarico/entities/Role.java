package com.gtarp.tabarico.entities;

import com.gtarp.tabarico.dto.RoleDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Role implements UpdatableEntity<Role, RoleDto> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String name;
    @Digits(integer = 3, fraction = 0)
    @Min(0)
    @Max(100)
    private Integer redistributionRate;
    @Digits(integer = 10, fraction = 0)
    private Integer salary;

    public Role(RoleDto roleDto) {
        this.name = roleDto.getName();
        this.redistributionRate = roleDto.getRedistributionRate();
        this.salary = roleDto.getSalary();
    }

    @Override
    public Role update(RoleDto roleDto) {
        this.name = roleDto.getName();
        this.redistributionRate = roleDto.getRedistributionRate();
        this.salary = roleDto.getSalary();
        return this;
    }
}
