package com.kazmierczak.daniel.car_auction_platform.repository;

import com.kazmierczak.daniel.car_auction_platform.models.entity.CarBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarBrandRepository extends JpaRepository<CarBrand, Long> {
    Optional<CarBrand> findByName(String name);
}
