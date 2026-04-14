package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dto.AuctionDto;

import java.util.List;

public interface AuctionService {
    List<AuctionDto> getAll();
    AuctionDto getById(Long id);
    AuctionDto saveAuction(AuctionDto auctionDto);
    void deleteById(Long id);
}
