package com.kazmierczak.daniel.car_auction_platform.models.entity;

import com.kazmierczak.daniel.car_auction_platform.models.enums.AuctionStatus;
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

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToOne
    @JoinColumn(name="vehicle_id")
    private Vehicle vehicle;

    @Column(name="start_price")
    private BigDecimal startPrice;

    @Column(name="current_price")
    private BigDecimal currentPrice;

    @Column(name="min_increment")
    private BigDecimal minIncrement;

    @Column(name="create_time")
    private LocalDateTime createTime;

    @Column(name="end_time")
    private LocalDateTime endTime;

    @Version
    @Column(name="version")
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private AuctionStatus status;
}