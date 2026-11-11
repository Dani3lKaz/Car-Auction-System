package com.kazmierczak.daniel.car_auction_platform.model.dto.vehicle;

import com.kazmierczak.daniel.car_auction_platform.model.enums.FuelType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVehicleDTO {

    @NotBlank(message = "Marka jest wymagana")
    private String brand;

    @NotBlank(message = "Model jest wymagany")
    private String model;

    @NotNull(message = "Rok produkcji jest wymagany")
    @Min(value = 1886, message = "Nieprawidłowy rok produkcji")
    @Max(value = 2100, message = "Nieprawidłowy rok produkcji")
    private Integer year;

    @NotNull(message = "Rodzaj paliwa jest wymagany")
    private FuelType fuelType;

    @NotNull(message = "Pojemność silnika jest wymagana")
    @Positive(message = "Pojemność silnika musi być większa od zera")
    private Integer engineCapacity;

    private String description;

    @NotBlank(message = "Numer VIN jest wymagany")
    private String vin;
}
