package com.kazmierczak.daniel.car_auction_platform.models.dto;

import com.kazmierczak.daniel.car_auction_platform.models.enums.FuelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDto {
    private Long id;
    private CarBrandDTO brand;
    private String model;
    private Integer year;
    private FuelType fuelType;
    private Integer engineCapacity;
    private String description;
    private String vin;
    private String image;
}
