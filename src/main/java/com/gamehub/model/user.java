package com.gamehub.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String email;
    private String password; 
    private String fullName;
    private String role; // ADMIN, USER
    private boolean banned = false;

    @ManyToMany
    private List<Product> wishlist = new ArrayList<>();
}
