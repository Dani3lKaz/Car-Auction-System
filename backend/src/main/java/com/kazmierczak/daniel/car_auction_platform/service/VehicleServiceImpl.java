package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.exception.VehicleVinAlreadyExistsException;
import com.kazmierczak.daniel.car_auction_platform.repository.VehicleRepository;
import com.kazmierczak.daniel.car_auction_platform.dto.VehicleDto;
import com.kazmierczak.daniel.car_auction_platform.entity.Vehicle;
import com.kazmierczak.daniel.car_auction_platform.mapper.VehicleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    public List<VehicleDto> getAll() {
        return vehicleRepository.findAll().stream()
                .map(VehicleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleDto getById(Long id) {
        Optional<Vehicle> result = vehicleRepository.findById(id);
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Vehicle with id " + id + " not found.");
        }
        return VehicleMapper.toDto(result.get());
    }

    @Override
    public VehicleDto saveVehicle(VehicleDto vehicleDto) {
        Vehicle vehicle = VehicleMapper.toEntity(vehicleDto);
        if (vehicle.getId() == null && vehicleRepository.findByVin(vehicle.getVin()).isPresent()) {
            throw new VehicleVinAlreadyExistsException("Vehicle with vin " + vehicle.getVin() + " already exists.");
        }
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return VehicleMapper.toDto(savedVehicle);
    }

    @Override
    public void deleteById(Long id) {
        if(!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Vehicle with id " + id + " not found.");
        }
        vehicleRepository.deleteById(id);
    }
}
