package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.repository.VehicleRepository;
import com.kazmierczak.daniel.car_auction_platform.dto.VehicleDto;
import com.kazmierczak.daniel.car_auction_platform.entity.Vehicle;
import com.kazmierczak.daniel.car_auction_platform.mapper.VehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<VehicleDto> getAll() {
        return vehicleRepository.findAll().stream()
                .map(VehicleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleDto getById(Long id) {
        Optional<Vehicle> result = vehicleRepository.findById(id);
        if (result.isPresent()) {
            return VehicleMapper.toDto(result.get());
        } else {
            throw new RuntimeException("Did not find vehicle id - " + id);
        }
    }

    @Override
    public VehicleDto saveVehicle(VehicleDto vehicleDto) {
        Vehicle vehicle = VehicleMapper.toEntity(vehicleDto);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return VehicleMapper.toDto(savedVehicle);
    }

    @Override
    public void deleteById(Long id) {
        vehicleRepository.deleteById(id);
    }
}
