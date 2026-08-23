package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.models.dto.car_brand.CarBrandDTO;
import com.kazmierczak.daniel.car_auction_platform.service.CarBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class CarBrandRestController {
    private CarBrandService carBrandService;

    @Autowired
    public CarBrandRestController(CarBrandService carBrandService) {this.carBrandService = carBrandService;}

    @GetMapping()
    public List<CarBrandDTO> getAll() {
        return carBrandService.getAll();
    }
}
