package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.ContractDto;
import com.gtarp.tabarico.entities.Contract;
import com.gtarp.tabarico.exception.ContractAlreadyExistException;
import com.gtarp.tabarico.exception.ContractNotFoundException;
import com.gtarp.tabarico.repositories.ContractRepository;
import com.gtarp.tabarico.services.impl.ContractServiceImpl;
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
public class ContractServiceTest {
    @InjectMocks
    private ContractServiceImpl contractService;
    @Mock
    private final ContractRepository contractRepository = mock(ContractRepository.class);

    @Test
    public void getAllContractsTest() {
        //GIVEN this should return a list
        List<Contract> expectedContractList = new ArrayList<>();
        when(contractRepository.findAll()).thenReturn(expectedContractList);

        //WHEN we call the method
        List<Contract> actualContractList = contractService.getAll();

        //THEN the correct method is called and we get the correct return
        assertEquals(expectedContractList, actualContractList);
        verify(contractRepository, times(1)).findAll();
    }

    @Test
    public void getContractByIdTest() {
        //GIVEN this should return a contract
        Contract expectedContract = new Contract();
        when(contractRepository.findById(anyInt())).thenReturn(Optional.of(expectedContract));

        //WHEN we try to get this contract
        Contract actualContract = contractService.getById(1);

        //THEN contractRepository.findById is called and we get the correct return
        assertEquals(expectedContract, actualContract);
        verify(contractRepository, times(1)).findById(anyInt());
    }

    @Test
    public void getContractByIdWhenContractIsNotFoundTest() {
        //GIVEN this should not find a contract
        when(contractRepository.findById(anyInt())).thenReturn(Optional.empty());

        //WHEN we try to get this contract THEN an exception is thrown
        assertThrows(ContractNotFoundException.class, () -> contractService.getById(1));
    }

    @Test
    public void addContractTest() {
        //GIVEN the contract we would add doesn't exist
        when(contractRepository.findContractByCompany(anyString())).thenReturn(Optional.empty());
        ContractDto contractDto = new ContractDto(1, "testCompany", 10);
        Contract contract = new Contract();
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);

        //WHEN we try to add this contract
        contractService.insert(contractDto);

        //THEN contractRepository.save is called
        verify(contractRepository, times(1)).save(any(Contract.class));
    }

    @Test
    public void addContractWhenContractAlreadyExistsTest() {
        //GIVEN the contract we would add already exist
        Contract contract = new Contract();
        ContractDto contractDto = new ContractDto(1, "testCompany", 10);
        when(contractRepository.findContractByCompany(anyString())).thenReturn(Optional.of(contract));

        //WHEN we try to add the contract THEN an exception is thrown
        assertThrows(ContractAlreadyExistException.class, () -> contractService.insert(contractDto));
    }

    @Test
    public void updateContractTest() {
        //GIVEN there is a contract to update
        Contract existingContract = new Contract();
        when(contractRepository.findById(anyInt())).thenReturn(Optional.of(existingContract));
        ContractDto contractDto = new ContractDto(1, "testCompany", 10);

        //WHEN we try to update the contract
        contractService.update(1, contractDto);

        //THEN contractRepository.save is called
        verify(contractRepository, times(1)).save(any(Contract.class));
    }

    @Test
    public void deleteContractTest() {
        //GIVEN there is a contract to delete
        Contract existingContract = new Contract();
        when(contractRepository.findById(anyInt())).thenReturn(Optional.of(existingContract));
        doNothing().when(contractRepository).delete(any(Contract.class));

        //WHEN we try to delete the contract
        contractService.delete(1);

        //THEN contractRepository.delete is called
        verify(contractRepository, times(1)).delete(any(Contract.class));
    }
}
