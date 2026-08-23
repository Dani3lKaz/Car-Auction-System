package com.kazmierczak.daniel.car_auction_platform.models.dto.auction;

import com.kazmierczak.daniel.car_auction_platform.models.dto.vehicle.CreateVehicleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAuctionDTO {
    private Long userId;
    private CreateVehicleDTO vehicle;
    private BigDecimal startPrice;
    private BigDecimal minIncrement;
    private LocalDateTime endTime;
}