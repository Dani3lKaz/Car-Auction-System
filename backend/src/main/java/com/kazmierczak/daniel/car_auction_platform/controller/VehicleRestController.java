package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.dto.VehicleDto;
import com.kazmierczak.daniel.car_auction_platform.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return vehicleService.getById(vehicleId);
    }

    @PostMapping
    public ResponseEntity<VehicleDto> addVehicle(@RequestBody VehicleDto vehicleDto){
        vehicleDto.setId(null);
        VehicleDto saved = vehicleService.saveVehicle(vehicleDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping
    public ResponseEntity<VehicleDto> updateVehicle(@RequestBody VehicleDto vehicleDto){
        VehicleDto saved = vehicleService.saveVehicle(vehicleDto);
        return ResponseEntity.status(HttpStatus.OK).body(saved);
    }

    @DeleteMapping("/{vehicleId}")
    public String deleteVehicle(@PathVariable Long vehicleId){
        vehicleService.deleteById(vehicleId);
        return "Deleted vehicle id - " + vehicleId;
    }
}
