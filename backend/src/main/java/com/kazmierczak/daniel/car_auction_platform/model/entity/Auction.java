package com.kazmierczak.daniel.car_auction_platform.model.entity;

import com.kazmierczak.daniel.car_auction_platform.model.enums.AuctionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="auctions")
@Getter
@Setter
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

    @OneToOne(cascade = CascadeType.PERSIST)
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

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private AuctionStatus status;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bid> bids;
}