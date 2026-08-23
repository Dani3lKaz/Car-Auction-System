package com.kazmierczak.daniel.car_auction_platform.mapper;

import com.kazmierczak.daniel.car_auction_platform.models.dto.car_brand.CarBrandDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.car_brand.CreateCarBrandDTO;
import com.kazmierczak.daniel.car_auction_platform.models.entity.CarBrand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarBrandMapper {
    private final VehicleMapper vehicleMapper;


    public CarBrandDTO toDTO(CarBrand carBrand) {
        return CarBrandDTO.builder()
                .id(carBrand.getId())
                .name(carBrand.getName())
                .vehicles(carBrand.getVehicles().stream().map(vehicleMapper::toDTO).toList())
                .build();
    }

    public CarBrand toEntity(CarBrandDTO carBrandDTO) {
        return CarBrand.builder()
                .id(carBrandDTO.getId())
                .name(carBrandDTO.getName())
                .build();
    }

    public CarBrand toEntity(CreateCarBrandDTO createCarBrandDTO) {
        return CarBrand.builder()
                .name(createCarBrandDTO.getName())
                .build();
    }
}
