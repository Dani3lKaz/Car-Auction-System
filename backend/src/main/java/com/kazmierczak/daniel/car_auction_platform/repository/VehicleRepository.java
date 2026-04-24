package com.kazmierczak.daniel.car_auction_platform.repository;

import com.kazmierczak.daniel.car_auction_platform.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<String> findByVin(String vin);
}
