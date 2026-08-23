package com.kazmierczak.daniel.car_auction_platform.models.dto.bid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBidDTO {
    private Long auctionId;
    private Long userId;
    private BigDecimal amount;
}