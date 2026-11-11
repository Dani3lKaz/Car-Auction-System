package com.kazmierczak.daniel.car_auction_platform.mapper;

import com.kazmierczak.daniel.car_auction_platform.model.dto.carbrand.CarBrandDTO;
import com.kazmierczak.daniel.car_auction_platform.model.entity.CarBrand;
import org.springframework.stereotype.Component;

@Component
public class CarBrandMapper {

    public CarBrandDTO toDTO(CarBrand carBrand) {
        return CarBrandDTO.builder()
                .id(carBrand.getId())
                .name(carBrand.getName())
                .build();
    }
}
