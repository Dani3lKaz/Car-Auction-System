package com.kazmierczak.daniel.car_auction_platform.mapper;

import com.kazmierczak.daniel.car_auction_platform.dto.BidDto;
import com.kazmierczak.daniel.car_auction_platform.entity.Bid;

public class BidMapper {

    public static BidDto toDto(Bid bid) {
        if (bid == null) {
            return null;
        }
        return BidDto.builder()
                .id(bid.getId())
                .auction(AuctionMapper.toDto(bid.getAuction()))
                .user(UserMapper.toDto(bid.getUser()))
                .amount(bid.getAmount())
                .createdAt(bid.getCreatedAt())
                .build();
    }

    public static Bid toEntity(BidDto dto) {
        if (dto == null) {
            return null;
        }
        return Bid.builder()
                .id(dto.getId())
                .auction(AuctionMapper.toEntity(dto.getAuction()))
                .user(UserMapper.toEntity(dto.getUser()))
                .amount(dto.getAmount())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
