package com.gtarp.tabarico.entities.accounting;

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
public class ExporterSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Calendar date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Integer quantity;
    private Integer level;
    private BigDecimal employeeAmount;
    private BigDecimal companyAmount;
}
