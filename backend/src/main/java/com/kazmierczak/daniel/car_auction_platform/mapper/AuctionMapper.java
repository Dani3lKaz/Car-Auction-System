package com.kazmierczak.daniel.car_auction_platform.mapper;

import com.kazmierczak.daniel.car_auction_platform.dto.AuctionDto;
import com.kazmierczak.daniel.car_auction_platform.entity.Auction;

public class AuctionMapper {

    public static AuctionDto toDto(Auction auction) {
        if (auction == null) {
            return null;
        }
        return AuctionDto.builder()
                .id(auction.getId())
                .vehicle(VehicleMapper.toDto(auction.getVehicle()))
                .startPrice(auction.getStartPrice())
                .currentPrice(auction.getCurrentPrice())
                .minIncrement(auction.getMinIncrement())
                .endTime(auction.getEndTime())
                .version(auction.getVersion())
                .status(auction.getStatus())
                .build();
    }

    public static Auction toEntity(AuctionDto dto) {
        if (dto == null) {
            return null;
        }
        return Auction.builder()
                .id(dto.getId())
                .vehicle(VehicleMapper.toEntity(dto.getVehicle()))
                .startPrice(dto.getStartPrice())
                .currentPrice(dto.getCurrentPrice())
                .minIncrement(dto.getMinIncrement())
                .endTime(dto.getEndTime())
                .version(dto.getVersion())
                .status(dto.getStatus())
                .build();
    }
}
