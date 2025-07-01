package com.gtarp.tabarico.repositories.accounting;

import com.gtarp.tabarico.entities.User;
import com.gtarp.tabarico.entities.accounting.ExporterSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExporterSaleRepository extends JpaRepository<ExporterSale, Integer> {
    List<ExporterSale> findAllByUserAndDateBetween(User user, LocalDateTime from, LocalDateTime to);
}
