package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.ContractDto;
import com.gtarp.tabarico.entities.Contract;

import java.util.List;

public interface ContractService {
    /**
     * get all the contracts in database
     * @return a list of contract
     */
    List<Contract> getAllContracts();

    /**
     * get a contract by its id
     * @param id the id of the contract
     * @return the contract
     */
    Contract getContractById(int id);

    /**
     * add a contract to database
     * @param contractDto the information of the contract to add
     * @return the contract
     */
    Contract addContract(ContractDto contractDto);

    /**
     * update a contract in database
     * @param id the id of the contract to update
     * @param contractDto the information of the updated contract
     * @return a contract
     */
    Contract updateContract(int id, ContractDto contractDto);

    /**
     * delete a contract from the database
     * @param id the id of the contract to delete
     */
    void deleteContract(int id);
}
