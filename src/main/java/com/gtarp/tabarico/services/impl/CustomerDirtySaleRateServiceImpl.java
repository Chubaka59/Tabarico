package com.gtarp.tabarico.services.impl;

import com.gtarp.tabarico.dto.CustomerDirtySaleRateDto;
import com.gtarp.tabarico.entities.CustomerDirtySaleRate;
import com.gtarp.tabarico.exception.CustomerDirtySaleRateNotFoundException;
import com.gtarp.tabarico.repositories.CustomerDirtySaleRateRepository;
import com.gtarp.tabarico.services.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class CustomerDirtySaleRateServiceImpl extends AbstractCrudService<CustomerDirtySaleRate, CustomerDirtySaleRateRepository, CustomerDirtySaleRateDto> {

    public CustomerDirtySaleRateServiceImpl(CustomerDirtySaleRateRepository repository) {
        super(repository);
    }

    @Override
    public CustomerDirtySaleRate getById(Integer id) {
        return this.repository.findById(id).orElseThrow(() -> new CustomerDirtySaleRateNotFoundException(id));
    }

    @Override
    public CustomerDirtySaleRate insert(CustomerDirtySaleRateDto customerDirtySaleRateDto) {
        return null;
    }
}
