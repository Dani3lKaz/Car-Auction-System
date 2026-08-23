package com.kazmierczak.daniel.car_auction_platform.mapper;

import com.kazmierczak.daniel.car_auction_platform.models.dto.vehicle.CreateVehicleDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.vehicle.VehicleDTO;
import com.kazmierczak.daniel.car_auction_platform.models.entity.CarBrand;
import com.kazmierczak.daniel.car_auction_platform.models.entity.Vehicle;
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

    public Vehicle toEntity(VehicleDTO vehicleDTO) {
        CarBrand brand = carBrandRepository.findByName(vehicleDTO.getBrand()).orElseThrow(() -> new EntityNotFoundException("Brand not found: " + vehicleDTO.getBrand()));

        return Vehicle.builder()
                .id(vehicleDTO.getId())
                .brand(brand)
                .model(vehicleDTO.getModel())
                .year(vehicleDTO.getYear())
                .fuelType(vehicleDTO.getFuelType())
                .engineCapacity(vehicleDTO.getEngineCapacity())
                .description(vehicleDTO.getDescription())
                .vin(vehicleDTO.getVin())
                .image(vehicleDTO.getImage())
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
                .image(createVehicleDTO.getImage())
                .build();
    }
}
