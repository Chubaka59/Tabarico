package com.gtarp.tabarico.services.impl;

import com.gtarp.tabarico.dto.ContractDto;
import com.gtarp.tabarico.entities.Contract;
import com.gtarp.tabarico.exception.ContractAlreadyExistException;
import com.gtarp.tabarico.exception.ContractNotFoundException;
import com.gtarp.tabarico.repositories.ContractRepository;
import com.gtarp.tabarico.services.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
    @Autowired
    private final ContractRepository contractRepository;

    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    @Override
    public Contract getContractById(int id) {
        return contractRepository.findById(id).orElseThrow(() -> new ContractNotFoundException(id));
    }

    @Override
    public Contract addContract(ContractDto contractDto) {
        Optional<Contract> existingContract = contractRepository.findContractByCompany(contractDto.getCompany());
        if (existingContract.isPresent()) {
            throw new ContractAlreadyExistException(contractDto.getCompany());
        }
        Contract newContract = new Contract(contractDto);
        return contractRepository.save(newContract);
    }

    @Override
    public Contract updateContract(int id, ContractDto contractDto) {
        Contract contract = getContractById(id).update(contractDto);
        return contractRepository.save(contract);
    }

    @Override
    public void deleteContract(int id) {
        Contract contract = getContractById(id);
        contractRepository.delete(contract);
    }
}
