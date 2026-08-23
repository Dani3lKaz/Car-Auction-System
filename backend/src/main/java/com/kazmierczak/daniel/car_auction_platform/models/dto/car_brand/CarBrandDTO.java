package com.kazmierczak.daniel.car_auction_platform.models.dto.car_brand;

import com.kazmierczak.daniel.car_auction_platform.models.dto.vehicle.VehicleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarBrandDTO {
    private Long id;
    private String name;
    private List<VehicleDTO> vehicles;
}