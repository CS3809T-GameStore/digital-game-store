package com.gamehub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "orders") // 'order' is a reserved SQL keyword
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String customerEmail;
    private String productName;
    private Double amountPaid;
    private String transactionId; // Virtual Payment ID
    private LocalDateTime orderDate = LocalDateTime.now();
}
