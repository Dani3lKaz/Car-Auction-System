package com.kazmierczak.daniel.car_auction_platform.mapper;

import com.kazmierczak.daniel.car_auction_platform.model.dto.vehicle.CreateVehicleDTO;
import com.kazmierczak.daniel.car_auction_platform.model.dto.vehicle.VehicleDTO;
import com.kazmierczak.daniel.car_auction_platform.model.entity.CarBrand;
import com.kazmierczak.daniel.car_auction_platform.model.entity.Vehicle;
import com.kazmierczak.daniel.car_auction_platform.repository.CarBrandRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleMapper {
    private final CarBrandRepository carBrandRepository;

    public VehicleDTO toDTO(Vehicle vehicle) {
        return VehicleDTO.builder()
                .id(vehicle.getId())
                .brand(vehicle.getBrand().getName())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .fuelType(vehicle.getFuelType())
                .engineCapacity(vehicle.getEngineCapacity())
                .description(vehicle.getDescription())
                .vin(vehicle.getVin())
                .image(vehicle.getImage())
                .build();
    }

    public Vehicle toEntity(CreateVehicleDTO createVehicleDTO) {
        CarBrand brand = carBrandRepository.findByName(createVehicleDTO.getBrand()).orElseThrow(() -> new EntityNotFoundException("Brand not found: " + createVehicleDTO.getBrand()));

        return Vehicle.builder()
                .brand(brand)
                .model(createVehicleDTO.getModel())
                .year(createVehicleDTO.getYear())
                .fuelType(createVehicleDTO.getFuelType())
                .engineCapacity(createVehicleDTO.getEngineCapacity())
                .description(createVehicleDTO.getDescription())
                .vin(createVehicleDTO.getVin())
                .build();
    }
}
