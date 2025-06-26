package com.gtarp.tabarico.entities.accounting;

import com.gtarp.tabarico.dto.accouting.OperationStock;
import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate date;
    private TypeOfStockMovement typeOfStockMovement;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private OperationStock operationStock;
    private Integer quantity;
    private Integer stock;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
