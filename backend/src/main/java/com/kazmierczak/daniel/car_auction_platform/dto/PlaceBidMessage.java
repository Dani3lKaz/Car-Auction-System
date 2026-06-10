package com.kazmierczak.daniel.car_auction_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceBidMessage {
    private Long auctionId;
    private BigDecimal amount;
}
