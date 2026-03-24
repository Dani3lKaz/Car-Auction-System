package com.kazmierczak.daniel.car_auction_platform.rest;

import com.kazmierczak.daniel.car_auction_platform.entity.Vehicle;
import com.kazmierczak.daniel.car_auction_platform.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleRestController {

    private VehicleService vehicleService;

    @Autowired
    public VehicleRestController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<Vehicle> getAll(){
        return vehicleService.getAll();
    }

    @GetMapping("/{vehicleId}")
    public Vehicle getVehicle(@PathVariable Long vehicleId) {
        Vehicle vehicle = vehicleService.getById(vehicleId);

        if(vehicle == null){
            throw new RuntimeException("Vehicle id not found - " +  vehicleId);
        }

        return vehicle;
    }

    @PostMapping
    public Vehicle addVehicle(@RequestBody Vehicle vehicle){
        vehicle.setId(null);

        return vehicleService.saveVehicle(vehicle);
    }

    @PutMapping
    public Vehicle updateVehicle(@RequestBody Vehicle vehicle){
        return vehicleService.saveVehicle(vehicle);
    }

    @DeleteMapping("/{vehicleId}")
    public  String deleteVehicle(@PathVariable Long vehicleId){
        Vehicle vehicle = vehicleService.getById(vehicleId);

        if(vehicle == null){
            throw new RuntimeException("Vehicle id not found - " +  vehicleId);
        }

        vehicleService.deleteById(vehicleId);

        return "Deleted vehicle id - " + vehicleId;
    }
}
