package com.kazmierczak.daniel.car_auction_platform.models.entity;

import com.kazmierczak.daniel.car_auction_platform.models.enums.FuelType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="brand_id")
    private CarBrand brand;

    @Column(name="model")
    private String model;

    @Column(name="year")
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(name="fuel_type")
    private FuelType fuelType;

    @Column(name="engine_capacity")
    private Integer engineCapacity;

    @Column(name="description")
    private String description;

    @Column(name="vin")
    private String vin;

    @Column(name="image")
    private String image;

}
