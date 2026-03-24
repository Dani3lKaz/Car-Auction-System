package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dao.VehicleRepository;
import com.kazmierczak.daniel.car_auction_platform.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService{

    private VehicleRepository vehicleRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }


    @Override
    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle getById(Long id) {
        Optional<Vehicle> result = vehicleRepository.findById(id);

        Vehicle vehicle = null;

        if(result.isPresent()) {
            vehicle = result.get();
        }else{
            throw new RuntimeException("Did not find vehicle id - " + id);
        }

        return  vehicle;
    }

    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public void deleteById(Long id) {
        vehicleRepository.deleteById(id);
    }
}
