package com.kazmierczak.daniel.car_auction_platform.models.dto.bid;

import com.kazmierczak.daniel.car_auction_platform.models.dto.auction.SimpleAuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.user.SimpleUserDTO;
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
public class BidDTO {
    private Long id;
    private SimpleAuctionDTO auction;
    private SimpleUserDTO user;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}