package com.gamehub.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    @Column(length = 5000) // Allow long descriptions
    private String description;
    
    private String category; // e.g., "Action", "FPS", "MMO"
    private String platform; // e.g., "Steam", "Xbox", "PSN"
    
    private Double priceUsd; // Base price in USD
    private String imageUrl;
    private LocalDate releaseDate;
    
    // "GAME" or "CARD" (for library separation)
    private String type; 
    
    private String secretKey; 
}
