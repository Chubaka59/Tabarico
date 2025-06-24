package com.gtarp.tabarico.services.impl;

import com.gtarp.tabarico.dto.ContractDto;
import com.gtarp.tabarico.entities.Contract;
import com.gtarp.tabarico.exception.ContractAlreadyExistException;
import com.gtarp.tabarico.exception.ContractNotFoundException;
import com.gtarp.tabarico.repositories.ContractRepository;
import com.gtarp.tabarico.services.AbstractCrudService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContractServiceImpl extends AbstractCrudService<Contract, ContractRepository, ContractDto> {

    public ContractServiceImpl(ContractRepository repository) {
        super(repository);
    }

    @Override
    public Contract getById(Integer id) {
        return this.repository.findById(id).orElseThrow(() -> new ContractNotFoundException(id));
    }

    @Override
    public Contract insert(ContractDto contractDto) {
        Optional<Contract> existingContract = this.repository.findContractByCompany(contractDto.getCompany());
        if (existingContract.isPresent()) {
            throw new ContractAlreadyExistException(contractDto.getCompany());
        }
        Contract newContract = new Contract(contractDto);
        return this.repository.save(newContract);
    }
}
