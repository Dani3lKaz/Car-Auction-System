package com.kazmierczak.daniel.car_auction_platform.model.dto.bid;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBidDTO {
    @NotNull(message = "Nie wybrano aukcji, dla której utworzono ofertę")
    private Long auctionId;
    @NotNull(message = "Kwota oferty jest wymagana")
    @Positive(message = "Kwota oferty musi być dodatnia")
    private BigDecimal amount;
}