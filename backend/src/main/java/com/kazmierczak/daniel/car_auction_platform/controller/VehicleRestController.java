package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.dto.VehicleDto;
import com.kazmierczak.daniel.car_auction_platform.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleRestController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleRestController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<VehicleDto> getAll(){
        return vehicleService.getAll();
    }

    @GetMapping("/{vehicleId}")
    public VehicleDto getVehicle(@PathVariable Long vehicleId) {
        VehicleDto vehicleDto = vehicleService.getById(vehicleId);

        if(vehicleDto == null){
            throw new RuntimeException("Vehicle id not found - " +  vehicleId);
        }

        return vehicleDto;
    }

    @PostMapping
    public VehicleDto addVehicle(@RequestBody VehicleDto vehicleDto){
        vehicleDto.setId(null);

        return vehicleService.saveVehicle(vehicleDto);
    }

    @PutMapping
    public VehicleDto updateVehicle(@RequestBody VehicleDto vehicleDto){
        return vehicleService.saveVehicle(vehicleDto);
    }

    @DeleteMapping("/{vehicleId}")
    public String deleteVehicle(@PathVariable Long vehicleId){
        VehicleDto vehicleDto = vehicleService.getById(vehicleId);

        if(vehicleDto == null){
            throw new RuntimeException("Vehicle id not found - " +  vehicleId);
        }

        vehicleService.deleteById(vehicleId);

        return "Deleted vehicle id - " + vehicleId;
    }
}
