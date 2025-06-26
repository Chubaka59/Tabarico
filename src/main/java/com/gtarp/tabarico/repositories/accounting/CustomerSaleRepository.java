package com.gtarp.tabarico.repositories.accounting;

import com.gtarp.tabarico.entities.accounting.CustomerSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerSaleRepository extends JpaRepository<CustomerSale, Integer> {
}
