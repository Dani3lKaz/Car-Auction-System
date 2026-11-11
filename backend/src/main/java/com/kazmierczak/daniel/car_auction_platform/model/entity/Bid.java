package com.kazmierczak.daniel.car_auction_platform.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="bids")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="auction_id")
    private Auction auction;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="created_at")
    private LocalDateTime createdAt;
}
