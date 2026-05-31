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


@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String[] args) throws Exception {

        if (userRepository.count() == 0) {
            User user1 = User.builder()
                    .firstName("Jan")
                    .lastName("Kowalski")
                    .email("jan.kowalski@example.com")
                    .password(passwordEncoder.encode("haslo123"))
                    .balance(new BigDecimal("150000.00"))
                    .role(Role.ADMIN)
                    .build();

            User user2 = User.builder()
                    .firstName("Anna")
                    .lastName("Nowak")
                    .email("anna.nowak@example.com")
                    .password(passwordEncoder.encode("haslo123"))
                    .balance(new BigDecimal("75000.00"))
                    .role(Role.USER)
                    .build();

            userRepository.saveAll(List.of(user1, user2));
            System.out.println("Loaded test users into the database");
        }

        if (vehicleRepository.count() == 0) {
            Vehicle vehicle1 = Vehicle.builder()
                    .brand("BMW")
                    .model("M3")
                    .year(2021)
                    .fuelType("Petrol")
                    .engineCapacity(2998)
                    .description("Test description")
                    .vin("WBA1234567890XYZ")
                    .image("https://example.com/bmw.jpg")
                    .build();

            Vehicle vehicle2 = Vehicle.builder()
                    .brand("Audi")
                    .model("RS6")
                    .year(2022)
                    .fuelType("Petrol")
                    .engineCapacity(3996)
                    .description("Test description")
                    .vin("WAU0987654321XYZ")
                    .image("https://example.com/audi.jpg")
                    .build();

            vehicleRepository.saveAll(List.of(vehicle1, vehicle2));
            System.out.println("Loaded test vehicles into the database");
        }
    }
}
