package com.kazmierczak.daniel.car_auction_platform.model.dto.auction;

import com.kazmierczak.daniel.car_auction_platform.model.dto.vehicle.CreateVehicleDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotNull(message = "Dane pojazdu są wymagane")
    @Valid
    private CreateVehicleDTO vehicle;

    @NotNull(message = "Cena wywoławcza jest wymagana")
    @Positive(message = "Cena wywoławcza musi być większa od zera")
    private BigDecimal startPrice;

    @NotNull(message = "Minimalne przebicie jest wymagane")
    @Positive(message = "Minimalne przebicie musi być większe od zera")
    private BigDecimal minIncrement;

    @NotNull(message = "Data zakończenia aukcji jest wymagana")
    @Future(message = "Data zakończenia aukcji musi być w przyszłości")
    private LocalDateTime endTime;
}
