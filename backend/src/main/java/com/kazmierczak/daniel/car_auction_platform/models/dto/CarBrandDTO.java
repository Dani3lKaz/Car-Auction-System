package com.kazmierczak.daniel.car_auction_platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarBrandDTO {
    private Long id;
    private String name;
}
