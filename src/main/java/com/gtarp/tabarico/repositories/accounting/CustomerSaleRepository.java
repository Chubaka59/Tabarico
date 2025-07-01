package com.gtarp.tabarico.repositories.accounting;

import com.gtarp.tabarico.entities.User;
import com.gtarp.tabarico.entities.accounting.CustomerSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerSaleRepository extends JpaRepository<CustomerSale, Integer> {
    List<CustomerSale> findAllByUserAndDateBetween(User user, LocalDateTime from, LocalDateTime to);
}
