package com.gamehub.repository;
import com.gamehub.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Product, Long> {}
