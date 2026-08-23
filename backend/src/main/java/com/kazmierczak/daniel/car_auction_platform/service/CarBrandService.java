package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.mapper.CarBrandMapper;
import com.kazmierczak.daniel.car_auction_platform.models.dto.car_brand.CarBrandDTO;
import com.kazmierczak.daniel.car_auction_platform.models.entity.CarBrand;
import com.kazmierczak.daniel.car_auction_platform.repository.CarBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarBrandService {

    private final CarBrandRepository carBrandRepository;
    private final CarBrandMapper carBrandMapper;

    public List<CarBrandDTO> getAll() {
        return carBrandRepository.findAll().stream().map(carBrandMapper::toDTO).collect(Collectors.toList());
    }

}
