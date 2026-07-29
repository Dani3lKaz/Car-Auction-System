package com.kazmierczak.daniel.car_auction_platform.mapper;

import com.kazmierczak.daniel.car_auction_platform.models.dto.CarBrandDTO;
import com.kazmierczak.daniel.car_auction_platform.models.entity.CarBrand;
import lombok.Builder;

public class CarBrandMapper {
    public static CarBrandDTO toDto(CarBrand carBrand){
        if(carBrand == null){
            return null;
        }
        return CarBrandDTO.builder()
                .id(carBrand.getId())
                .name(carBrand.getName())
                .build();
    }

    public static CarBrand toEntity(CarBrandDTO dto){
        if(dto == null){
            return null;
        }
        return CarBrand.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
