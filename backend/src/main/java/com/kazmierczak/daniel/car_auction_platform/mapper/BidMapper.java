package com.kazmierczak.daniel.car_auction_platform.mapper;

import com.kazmierczak.daniel.car_auction_platform.models.dto.bid.BidDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.bid.CreateBidDTO;
import com.kazmierczak.daniel.car_auction_platform.models.entity.Auction;
import com.kazmierczak.daniel.car_auction_platform.models.entity.Bid;
import com.kazmierczak.daniel.car_auction_platform.models.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BidMapper {

    private final UserMapper userMapper;
    @Lazy
    private final AuctionMapper auctionMapper;

    public BidDTO toDTO(Bid bid) {
        return BidDTO.builder()
                .id(bid.getId())
                .auction(auctionMapper.toSimpleDTO(bid.getAuction()))
                .user(userMapper.toSimpleDTO(bid.getUser()))
                .amount(bid.getAmount())
                .createdAt(bid.getCreatedAt())
                .build();
    }


    public Bid toEntity(BidDTO bidDTO) {
        return Bid.builder()
                .id(bidDTO.getId())
                .amount(bidDTO.getAmount())
                .createdAt(bidDTO.getCreatedAt())
                .build();
    }

    public Bid toEntity(CreateBidDTO createBidDTO, Auction auction, User user) {
        return Bid.builder()
                .amount(createBidDTO.getAmount())
                .auction(auction)
                .user(user)
                .build();
    }
}