package com.gamehub;

import com.gamehub.model.Product;
import com.gamehub.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initDatabase(ProductRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                Product p1 = new Product();
                p1.setTitle("Cyber Revolution");
                p1.setCategory("Action, RPG");
                p1.setPriceSar(150.00);
                p1.setSecretKey("CR-2025-KEY-X999");
                p1.setImageUrl("images/cyber.jpg"); // Make sure this image exists in static/images
                repository.save(p1);

                Product p2 = new Product();
                p2.setTitle("Fantasy Realms");
                p2.setCategory("Adventure");
                p2.setPriceSar(199.00);
                p2.setSecretKey("FR-LEGEND-888");
                p2.setImageUrl("images/fantasy.jpg");
                repository.save(p2);
            }
        };
    }
}
