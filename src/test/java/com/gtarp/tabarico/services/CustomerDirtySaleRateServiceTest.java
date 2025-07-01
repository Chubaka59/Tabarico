package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.CustomerDirtySaleRateDto;
import com.gtarp.tabarico.entities.CustomerDirtySaleRate;
import com.gtarp.tabarico.exception.CustomerDirtySaleRateNotFoundException;
import com.gtarp.tabarico.repositories.CustomerDirtySaleRateRepository;
import com.gtarp.tabarico.services.impl.CustomerDirtySaleRateServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CustomerDirtySaleRateServiceTest {
    @InjectMocks
    private CustomerDirtySaleRateServiceImpl customerDirtySaleRateService;
    @Mock
    private final CustomerDirtySaleRateRepository customerDirtySaleRateRepository = mock(CustomerDirtySaleRateRepository.class);

    @Test
    public void getAllCustomerDirtySaleRatesTest() {
        //GIVEN this should return a list
        List<CustomerDirtySaleRate> expectedCustomerDirtySaleRateList = new ArrayList<>();
        when(customerDirtySaleRateRepository.findAll()).thenReturn(expectedCustomerDirtySaleRateList);

        //WHEN we call the method
        List<CustomerDirtySaleRate> actualCustomerDirtySaleRateList = customerDirtySaleRateService.getAll();

        //THEN the correct method is called and we get the correct return
        assertEquals(expectedCustomerDirtySaleRateList, actualCustomerDirtySaleRateList);
        verify(customerDirtySaleRateRepository, times(1)).findAll();
    }

    @Test
    public void getCustomerDirtySaleRateByIdTest() {
        //GIVEN this should return a customerDirtySaleRate
        CustomerDirtySaleRate expectedCustomerDirtySaleRate = new CustomerDirtySaleRate();
        when(customerDirtySaleRateRepository.findById(anyInt())).thenReturn(Optional.of(expectedCustomerDirtySaleRate));

        //WHEN we try to get this customerDirtySaleRate
        CustomerDirtySaleRate actualCustomerDirtySaleRate = customerDirtySaleRateService.getById(1);

        //THEN customerDirtySaleRateRepository.findById is called and we get the correct return
        assertEquals(expectedCustomerDirtySaleRate, actualCustomerDirtySaleRate);
        verify(customerDirtySaleRateRepository, times(1)).findById(anyInt());
    }

    @Test
    public void getCustomerDirtySaleRateByIdWhenCustomerDirtySaleRateIsNotFoundTest() {
        //GIVEN this should not find a customerDirtySaleRate
        when(customerDirtySaleRateRepository.findById(anyInt())).thenReturn(Optional.empty());

        //WHEN we try to get this customerDirtySaleRate THEN an exception is thrown
        assertThrows(CustomerDirtySaleRateNotFoundException.class, () -> customerDirtySaleRateService.getById(1));
    }

    @Test
    public void updateCustomerDirtySaleRateTest() {
        //GIVEN there is a customerDirtySaleRate to update
        CustomerDirtySaleRate existingCustomerDirtySaleRate = new CustomerDirtySaleRate();
        when(customerDirtySaleRateRepository.findById(anyInt())).thenReturn(Optional.of(existingCustomerDirtySaleRate));
        CustomerDirtySaleRateDto customerDirtySaleRateDto = new CustomerDirtySaleRateDto(1, 10);

        //WHEN we try to update the customerDirtySaleRate
        customerDirtySaleRateService.update(1, customerDirtySaleRateDto);

        //THEN customerDirtySaleRateRepository.save is called
        verify(customerDirtySaleRateRepository, times(1)).save(any(CustomerDirtySaleRate.class));
    }

    @Test
    public void deleteCustomerDirtySaleRateTest() {
        //GIVEN there is a customerDirtySaleRate to delete
        CustomerDirtySaleRate existingCustomerDirtySaleRate = new CustomerDirtySaleRate();
        when(customerDirtySaleRateRepository.findById(anyInt())).thenReturn(Optional.of(existingCustomerDirtySaleRate));
        doNothing().when(customerDirtySaleRateRepository).delete(any(CustomerDirtySaleRate.class));

        //WHEN we try to delete the customerDirtySaleRate
        customerDirtySaleRateService.delete(1);

        //THEN customerDirtySaleRateRepository.delete is called
        verify(customerDirtySaleRateRepository, times(1)).delete(any(CustomerDirtySaleRate.class));
    }
}
