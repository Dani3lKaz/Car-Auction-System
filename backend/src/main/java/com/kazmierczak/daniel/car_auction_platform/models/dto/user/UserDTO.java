package com.kazmierczak.daniel.car_auction_platform.models.dto.user;

import com.kazmierczak.daniel.car_auction_platform.models.dto.auction.AuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.auction.SimpleAuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.bid.BidDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal balance;
    private List<SimpleAuctionDTO> auctions;
    private List<BidDTO> bids;
}