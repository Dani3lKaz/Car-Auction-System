package com.kazmierczak.daniel.car_auction_platform.entity;

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

    @Column(name="brand")
    private String brand;

    @Column(name="model")
    private String model;

    @Column(name="year")
    private Integer year;

    @Column(name="petrol")
    private String petrol;

    @Column(name="engine")
    private Integer engine;

    @Column(name="description")
    private String description;

    @Column(name="vin")
    private String vin;

    @Column(name="image")
    private String image;

}
