package com.kazmierczak.daniel.car_auction_platform.models.dto.bid;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBidDTO {
    @NotNull(message = "Auction Id is required")
    private Long auctionId;
    @NotNull(message = "Bid amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
}