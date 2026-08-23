package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.models.dto.vehicle.CreateVehicleDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.vehicle.VehicleDTO;
import com.kazmierczak.daniel.car_auction_platform.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleRestController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleRestController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/{vehicleId}")
    public VehicleDTO getVehicle(@PathVariable Long vehicleId) {
        return vehicleService.getById(vehicleId);
    }

    @PostMapping
    public ResponseEntity<VehicleDTO> addVehicle(@RequestBody CreateVehicleDTO vehicleDto){
        VehicleDTO saved = vehicleService.saveVehicle(vehicleDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
