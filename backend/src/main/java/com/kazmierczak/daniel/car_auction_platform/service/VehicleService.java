package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.exception.VehicleVinAlreadyExistsException;
import com.kazmierczak.daniel.car_auction_platform.mapper.VehicleMapper;
import com.kazmierczak.daniel.car_auction_platform.models.dto.vehicle.CreateVehicleDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.vehicle.VehicleDTO;
import com.kazmierczak.daniel.car_auction_platform.repository.VehicleRepository;
import com.kazmierczak.daniel.car_auction_platform.models.entity.Vehicle;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleDTO getById(Long id) {
        Optional<Vehicle> result = Optional.of(vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found: " + id)));

        return vehicleMapper.toDTO(result.get());
    }

    public VehicleDTO saveVehicle(CreateVehicleDTO vehicleDto) {
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto);
        if (vehicle.getId() == null && vehicleRepository.findByVin(vehicle.getVin()).isPresent()) {
            throw new VehicleVinAlreadyExistsException("Vehicle with vin " + vehicle.getVin() + " already exists.");
        }
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toDTO(savedVehicle);
    }
}