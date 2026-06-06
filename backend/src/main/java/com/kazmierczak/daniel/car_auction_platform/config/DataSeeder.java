package com.kazmierczak.daniel.car_auction_platform.config;

import com.kazmierczak.daniel.car_auction_platform.entity.Auction;
import com.kazmierczak.daniel.car_auction_platform.entity.Role;
import com.kazmierczak.daniel.car_auction_platform.entity.User;
import com.kazmierczak.daniel.car_auction_platform.entity.Vehicle;
import com.kazmierczak.daniel.car_auction_platform.repository.AuctionRepository;
import com.kazmierczak.daniel.car_auction_platform.repository.UserRepository;
import com.kazmierczak.daniel.car_auction_platform.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final AuctionRepository auctionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String[] args) throws Exception {

        if (userRepository.count() == 0) {
            User user1 = User.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .email("admin@mail.com")
                    .password(passwordEncoder.encode("Admin1234"))
                    .balance(new BigDecimal("100000.00"))
                    .role(Role.ADMIN)
                    .build();

            User user2 = User.builder()
                    .firstName("User")
                    .lastName("User")
                    .email("user@mail.com")
                    .password(passwordEncoder.encode("User1234"))
                    .balance(new BigDecimal("75000.00"))
                    .role(Role.USER)
                    .build();

            User seller = User.builder()
                    .firstName("Seller")
                    .lastName("Seller")
                    .email("seller@mail.com")
                    .password(passwordEncoder.encode("Seller1234"))
                    .balance(new BigDecimal("150000.00"))
                    .role(Role.SELLER)
                    .build();

            userRepository.saveAll(List.of(user1, user2, seller));
            System.out.println("Loaded test users into the database");
        }

        if (vehicleRepository.count() == 0 && auctionRepository.count() == 0) {
                User seller = userRepository.findByEmail("seller@mail.com").orElseThrow();
                Vehicle vehicle1 = Vehicle.builder()
                    .brand("Toyota")
                    .model("Corolla")
                    .year(2020)
                    .fuelType("Hybrid")
                    .engineCapacity(1500)
                    .description("Test description")
                    .vin("TESTVIN1")
                    .image("http://localhost:8080/seed-images/seed-1.png")
                    .build();

                Vehicle vehicle2 = Vehicle.builder()
                    .brand("BMW")
                    .model("Series 3")
                    .year(2002)
                    .fuelType("Petrol")
                    .engineCapacity(3000)
                    .description("Test description")
                    .vin("TESTVIN2")
                    .image("http://localhost:8080/seed-images/seed-2.png")
                    .build();

            vehicleRepository.saveAll(List.of(vehicle1, vehicle2));
            System.out.println("Loaded test vehicles into the database");

                Auction auction1 = Auction.builder()
                    .vehicle(vehicle1)
                    .seller(seller)
                    .startPrice(new BigDecimal("50000.00"))
                    .currentPrice(new BigDecimal("50000.00"))
                    .minIncrement(new BigDecimal("1000.00"))
                    .endTime(LocalDateTime.now().plusDays(1))
                    .status("ACTIVE")
                    .build();

                Auction auction2 = Auction.builder()
                    .vehicle(vehicle2)
                    .seller(seller)
                    .startPrice(new BigDecimal("12000.00"))
                    .currentPrice(new BigDecimal("12000.00"))
                    .minIncrement(new BigDecimal("500.00"))
                    .endTime(LocalDateTime.now().plusDays(1))
                    .status("ACTIVE")
                    .build();

                auctionRepository.saveAll(List.of(auction1, auction2));
                System.out.println("Loaded test auctions into the database");
        }
    }
}
