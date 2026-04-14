package com.kazmierczak.daniel.car_auction_platform.dto;

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
public class BidDto {
    private Long id;
    private AuctionDto auction;
    private UserDto user;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}
