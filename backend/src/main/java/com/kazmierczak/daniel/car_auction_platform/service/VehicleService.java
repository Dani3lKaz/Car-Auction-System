package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dto.VehicleDto;

import java.util.List;

public interface VehicleService {
    List<VehicleDto> getAll();
    VehicleDto getById(Long id);
    VehicleDto saveVehicle(VehicleDto vehicleDto);
    void deleteById(Long id);
}
