package com.kazmierczak.daniel.car_auction_platform.dto;

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
    private String brand;
    private String model;
    private Integer year;
    private String fuelType;
    private Integer engineCapacity;
    private String description;
    private String vin;
    private String image;
}
