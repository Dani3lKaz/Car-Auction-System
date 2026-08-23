package com.kazmierczak.daniel.car_auction_platform.mapper;

import com.kazmierczak.daniel.car_auction_platform.models.dto.auction.AuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.auction.CreateAuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.auction.SimpleAuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.models.entity.Auction;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionMapper {

    private final UserMapper userMapper;
    private final VehicleMapper vehicleMapper;

    public AuctionDTO toDTO(Auction auction) {
        return AuctionDTO.builder()
                .id(auction.getId())
                .user(userMapper.toSimpleDTO(auction.getUser()))
                .vehicle(vehicleMapper.toDTO(auction.getVehicle()))
                .startPrice(auction.getStartPrice())
                .currentPrice(auction.getCurrentPrice())
                .minIncrement(auction.getMinIncrement())
                .createTime(auction.getCreateTime())
                .endTime(auction.getEndTime())
                .status(auction.getStatus())
                .build();
    }

    public SimpleAuctionDTO toSimpleDTO(Auction auction) {
        return SimpleAuctionDTO.builder()
                .id(auction.getId())
                .currentPrice(auction.getCurrentPrice())
                .endTime(auction.getEndTime())
                .status(auction.getStatus())
                .vehicle(vehicleMapper.toDTO(auction.getVehicle()))
                .build();
    }

    public Auction toEntity(AuctionDTO auctionDTO) {
        return Auction.builder()
                .id(auctionDTO.getId())
                .startPrice(auctionDTO.getStartPrice())
                .currentPrice(auctionDTO.getCurrentPrice())
                .minIncrement(auctionDTO.getMinIncrement())
                .createTime(auctionDTO.getCreateTime())
                .endTime(auctionDTO.getEndTime())
                .status(auctionDTO.getStatus())
                .vehicle(vehicleMapper.toEntity(auctionDTO.getVehicle()))
                .build();
    }

    public Auction toEntity(CreateAuctionDTO createAuctionDTO) {
        return Auction.builder()
                .startPrice(createAuctionDTO.getStartPrice())
                .minIncrement(createAuctionDTO.getMinIncrement())
                .endTime(createAuctionDTO.getEndTime())
                .vehicle(vehicleMapper.toEntity(createAuctionDTO.getVehicle()))
                .build();
    }
}