package com.kazmierczak.daniel.car_auction_platform.models.dto.auction;

import com.kazmierczak.daniel.car_auction_platform.models.dto.vehicle.VehicleDTO;
import com.kazmierczak.daniel.car_auction_platform.models.enums.AuctionStatus;
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
public class SimpleAuctionDTO {
    private Long id;
    private BigDecimal currentPrice;
    private LocalDateTime endTime;
    private AuctionStatus status;
    private VehicleDTO vehicle;
}