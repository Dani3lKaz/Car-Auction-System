package com.kazmierczak.daniel.car_auction_platform.mapper;

import com.kazmierczak.daniel.car_auction_platform.dto.VehicleDto;
import com.kazmierczak.daniel.car_auction_platform.entity.Vehicle;

public class VehicleMapper {

    public static VehicleDto toDto(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return VehicleDto.builder()
                .id(vehicle.getId())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .fuelType(vehicle.getFuelType())
                .engineCapacity(vehicle.getEngineCapacity())
                .description(vehicle.getDescription())
                .vin(vehicle.getVin())
                .image(vehicle.getImage())
                .build();
    }

    public static Vehicle toEntity(VehicleDto dto) {
        if (dto == null) {
            return null;
        }
        return Vehicle.builder()
                .id(dto.getId())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .year(dto.getYear())
                .fuelType(dto.getFuelType())
                .engineCapacity(dto.getEngineCapacity())
                .description(dto.getDescription())
                .vin(dto.getVin())
                .image(dto.getImage())
                .build();
    }
}
