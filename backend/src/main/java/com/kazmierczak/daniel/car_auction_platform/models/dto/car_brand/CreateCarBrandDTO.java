package com.kazmierczak.daniel.car_auction_platform.models.dto.car_brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCarBrandDTO {
    private String name;
}