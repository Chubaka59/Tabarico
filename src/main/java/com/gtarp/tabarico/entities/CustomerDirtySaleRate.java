package com.gtarp.tabarico.entities;

import com.gtarp.tabarico.dto.CustomerDirtySaleRateDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDirtySaleRate implements UpdatableEntity<CustomerDirtySaleRate, CustomerDirtySaleRateDto> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Digits(integer = 3, fraction = 0)
    @Min(0)
    @Max(100)
    private Integer customerDirtySaleRate;

    @Override
    public CustomerDirtySaleRate update(CustomerDirtySaleRateDto customerDirtySaleRateDto) {
        this.customerDirtySaleRate = customerDirtySaleRateDto.getCustomerDirtySaleRate();
        return this;
    }
}
