package com.gamehub.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String category; // e.g., "Action, Adventure"
    private Double priceSar;
    private String imageUrl;
    
    // Virtual License Key (Simplification for Uni Project: 1 key per product reused, 
    // or you can make a separate table for unique keys)
    private String secretKey; 
}
