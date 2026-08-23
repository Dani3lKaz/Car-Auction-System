package com.kazmierczak.daniel.car_auction_platform.models.dto.auction;

import com.kazmierczak.daniel.car_auction_platform.models.dto.bid.BidDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.user.SimpleUserDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.vehicle.VehicleDTO;
import com.kazmierczak.daniel.car_auction_platform.models.enums.AuctionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionDTO {
    private Long id;
    private SimpleUserDTO user;
    private VehicleDTO vehicle;
    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private BigDecimal minIncrement;
    private LocalDateTime createTime;
    private LocalDateTime endTime;
    private AuctionStatus status;
    private List<BidDTO> bids;
}