package com.gtarp.tabarico.repositories.accounting;

import com.gtarp.tabarico.entities.accounting.ExporterSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExporterSaleRepository extends JpaRepository<ExporterSale, Integer> {
}
