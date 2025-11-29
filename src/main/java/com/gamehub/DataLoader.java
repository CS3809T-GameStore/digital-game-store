package com.gamehub;

import com.gamehub.model.Product;
import com.gamehub.model.User;
import com.gamehub.repository.ProductRepository;
import com.gamehub.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Random;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepo, UserRepository userRepo) {
        return args -> {
            // 1. Create Admin
            if (userRepo.findByEmail("Admin@gamehub.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("Admin@gamehub.com");
                admin.setPassword("Admin@30"); 
                admin.setFullName("Super Admin");
                admin.setRole("ADMIN");
                userRepo.save(admin);
                System.out.println("ADMIN ACCOUNT CREATED");
            }

            // 2. Check if games exist, if not, generate them
            if (productRepo.count() == 0) {
                // ACTION GAMES (Real Data)
                createGame(productRepo, "Grand Theft Auto V: Premium Edition", 
                    "Get the complete Grand Theft Auto V experience: the award-winning Story Mode and the massive, evolving world of Grand Theft Auto Online. Includes Criminal Enterprise Starter Pack.",
                    "Action", "Steam", 29.99, "https://upload.wikimedia.org/wikipedia/en/a/a5/Grand_Theft_Auto_V.png", LocalDate.of(2018, 4, 20));

                createGame(productRepo, "Red Dead Redemption 2", 
                    "Winner of over 175 Game of the Year Awards and recipient of over 250 perfect scores, RDR2 is the epic tale of outlaw Arthur Morgan and the infamous Van der Linde gang.",
                    "Action", "Steam", 59.99, "https://upload.wikimedia.org/wikipedia/en/4/44/Red_Dead_Redemption_II.jpg", LocalDate.of(2018, 10, 26));

                createGame(productRepo, "Elden Ring", 
                    "THE NEW FANTASY ACTION RPG. Rise, Tarnished, and be guided by grace to brandish the power of the Elden Ring and become an Elden Lord in the Lands Between.",
                    "RPG", "Steam", 59.99, "https://upload.wikimedia.org/wikipedia/en/b/b9/Elden_Ring_Box_Art.jpg", LocalDate.of(2022, 2, 25));

                // FPS GAMES
                createGame(productRepo, "Call of Duty: Modern Warfare III", 
                    "In the direct sequel to the record-breaking Modern Warfare II, Captain Price and Task Force 141 face off against the ultimate threat.",
                    "FPS", "Battle.net", 69.99, "https://upload.wikimedia.org/wikipedia/en/thumb/b/b7/Call_of_Duty_Modern_Warfare_III_2023_cover_art.jpg/220px-Call_of_Duty_Modern_Warfare_III_2023_cover_art.jpg", LocalDate.of(2023, 11, 10));

                // CARDS
                createCard(productRepo, "Xbox Game Pass Ultimate - 1 Month", 
                    "Play over 100 high-quality games with friends on console, PC, phones and tablets.", 
                    "Cards", "Xbox", 16.99, "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0f/Xbox_Game_Pass_logo.svg/1200px-Xbox_Game_Pass_logo.svg.png");

                createCard(productRepo, "PlayStation Plus Premium - 12 Months", 
                    "Enjoy all core PlayStation Plus benefits, hundreds of games in the Game Catalog, as well as exclusive benefits.", 
                    "Cards", "PSN", 159.99, "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/PlayStation_Plus_logo_2022.svg/2560px-PlayStation_Plus_logo_2022.svg.png");

                // GENERATE BULK (To meet "20-40 games" requirement)
                generateBulkGames(productRepo, "Cyberpunk 2077", "RPG", 40.00, 20);
                generateBulkGames(productRepo, "FIFA 24 FC", "Sports", 50.00, 20);
                generateBulkGames(productRepo, "Minecraft Java", "Arcade", 25.00, 15);
            }
        };
    }

    private void createGame(ProductRepository repo, String title, String desc, String cat, String platform, double price, String img, LocalDate date) {
        Product p = new Product();
        p.setTitle(title);
        p.setDescription(desc);
        p.setCategory(cat);
        p.setPlatform(platform);
        p.setPriceUsd(price);
        p.setImageUrl(img);
        p.setReleaseDate(date);
        p.setType("GAME");
        p.setSecretKey("KEY-" + title.hashCode() + "-123");
        repo.save(p);
    }

    private void createCard(ProductRepository repo, String title, String desc, String cat, String platform, double price, String img) {
        Product p = new Product();
        p.setTitle(title);
        p.setDescription(desc);
        p.setCategory(cat);
        p.setPlatform(platform);
        p.setPriceUsd(price);
        p.setImageUrl(img);
        p.setType("CARD");
        p.setReleaseDate(LocalDate.now());
        p.setSecretKey("CODE-" + System.currentTimeMillis());
        repo.save(p);
    }

    private void generateBulkGames(ProductRepository repo, String baseTitle, String cat, double basePrice, int count) {
        Random rand = new Random();
        for (int i = 1; i <= count; i++) {
            Product p = new Product();
            p.setTitle(baseTitle + " - Bundle " + i);
            p.setDescription("Generated bundle variation for " + baseTitle + ". Includes special skins and bonus currency.");
            p.setCategory(cat);
            p.setPlatform("Steam");
            p.setPriceUsd(basePrice + rand.nextInt(20)); // Random price variation
            p.setImageUrl("https://placehold.co/400x600?text=" + baseTitle.replace(" ", "+") + "+" + i);
            p.setReleaseDate(LocalDate.now().minusDays(rand.nextInt(1000)));
            p.setType("GAME");
            p.setSecretKey("KEY-BULK-" + i);
            repo.save(p);
        }
    }
}
