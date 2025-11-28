package com.gamehub.controller;

import com.gamehub.model.*;
import com.gamehub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ShopController {

    @Autowired
    private OrderRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    // 1. List all games
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 2. Virtual Payment & Checkout
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody Map<String, Object> payload) {
        // Extract data from frontend
        Long productId = Long.valueOf(payload.get("productId").toString());
        String email = (String) payload.get("email");
        String cardNumber = (String) payload.get("cardNumber"); // Virtual Card

        // VIRTUAL PAYMENT LOGIC
        if (cardNumber == null || cardNumber.length() < 16) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid Card Number"));
        }

        Product product = productRepository.findById(productId).orElseThrow();

        // Create Order
        Order order = new Order();
        order.setCustomerEmail(email);
        order.setProductName(product.getTitle());
        order.setAmountPaid(product.getPriceSar());
        order.setTransactionId(UUID.randomUUID().toString()); // Fake Trans ID
        
        orderRepository.save(order);

        // Return the "Instant Delivery" Key
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Payment Successful!",
            "gameKey", product.getSecretKey(), // The instant delivery magic
            "transactionId", order.getTransactionId()
        ));
    }
}
