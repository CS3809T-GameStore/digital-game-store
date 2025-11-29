package com.gamehub.controller;

import com.gamehub.model.*;
import com.gamehub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ShopController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // --- PUBLIC ENDPOINTS ---

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                if (user.isBanned()) return ResponseEntity.status(403).body(Map.of("error", "Account Banned"));
                
                return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "role", user.getRole(),
                    "email", user.getEmail(),
                    "name", user.getFullName()
                ));
            }
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody Map<String, Object> payload) {
        try {
            Object idObj = payload.get("productId");
            if (idObj == null) return ResponseEntity.badRequest().body(Map.of("error", "ID missing"));
            long productId = Long.parseLong(idObj.toString());

            String email = (String) payload.get("email");
            String cardNumber = (String) payload.get("cardNumber");

            // Validate Card (Simple length check for virtual payment)
            if (cardNumber == null || cardNumber.length() < 12) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid Card"));
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Order order = new Order();
            order.setCustomerEmail(email);
            order.setProductName(product.getTitle());
            order.setAmountPaid(product.getPriceUsd()); 
            order.setTransactionId(UUID.randomUUID().toString()); // Fake Trans ID
            
            orderRepository.save(order);

            return ResponseEntity.ok(Map.of("status", "success", "gameKey", product.getSecretKey()));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/my-library")
    public List<Order> getUserLibrary(@RequestParam String email) {
        return orderRepository.findByCustomerEmail(email);
    }

    // --- WISHLIST ENDPOINTS ---
    
    @PostMapping("/wishlist/add")
    public ResponseEntity<?> addToWishlist(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        Object pId = payload.get("productId");
        if(email == null || pId == null) return ResponseEntity.badRequest().build();

        Long productId = Long.valueOf(pId.toString());
        
        User user = userRepository.findByEmail(email).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();
        
        // Prevent duplicates
        if(!user.getWishlist().contains(product)) {
            user.getWishlist().add(product);
            userRepository.save(user);
        }
        return ResponseEntity.ok(Map.of("status", "added"));
    }
    
    @GetMapping("/wishlist")
    public List<Product> getWishlist(@RequestParam String email) {
        return userRepository.findByEmail(email)
                .map(User::getWishlist)
                .orElse(List.of());
    }

    // --- ADMIN ENDPOINTS ---

    @GetMapping("/admin/sales")
    public ResponseEntity<?> getSalesStats() {
        long count = orderRepository.count();
        double totalRevenue = orderRepository.findAll().stream()
                .mapToDouble(Order::getAmountPaid).sum();
        long userCount = userRepository.count();
                
        return ResponseEntity.ok(Map.of(
            "totalOrders", count,
            "totalRevenue", totalRevenue,
            "totalUsers", userCount
        ));
    }

    @PostMapping("/admin/product")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        // Generate a fake key if none provided
        if(product.getSecretKey() == null) {
            product.setSecretKey("NEW-KEY-" + System.currentTimeMillis());
        }
        productRepository.save(product);
        return ResponseEntity.ok(Map.of("status", "success"));
    }
}
