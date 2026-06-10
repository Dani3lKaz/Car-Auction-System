package com.kazmierczak.daniel.car_auction_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceBidResult {
    private BidDto savedBid;
    private String outbidUserEmail;
    private BigDecimal outbidAmount;
}
