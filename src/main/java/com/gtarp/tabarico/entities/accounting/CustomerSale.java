package com.gtarp.tabarico.entities.accounting;

import com.gtarp.tabarico.entities.Contract;
import com.gtarp.tabarico.entities.Product;
import com.gtarp.tabarico.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Calendar date;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private TypeOfSale typeOfSale;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
