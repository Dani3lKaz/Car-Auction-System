package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.entity.Vehicle;

import java.util.List;

public interface VehicleService {

    List<Vehicle> getAll();

    Vehicle getById(Long id);

    Vehicle saveVehicle(Vehicle vehicle);

    void deleteById(Long id);

}
