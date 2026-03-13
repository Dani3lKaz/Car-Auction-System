package com.kazmierczak.daniel.car_auction_platform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="auctions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="vehicle_id")
    private Long vehicleId;

    @Column(name="start_price")
    private BigDecimal startPrice;

    @Column(name="current_price")
    private BigDecimal currentPrice;

    @Column(name="min_increment")
    private BigDecimal minIncrement;

    @Column(name="end_time")
    private LocalDateTime endTime;

    @Column(name="version")
    private Long version;

    @Column(name="status")
    private String status;
}
