package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dto.BidDto;
import com.kazmierczak.daniel.car_auction_platform.dto.PlaceBidResult;

import java.math.BigDecimal;
import java.util.List;

public interface BidService {
    List<BidDto> getAll();
    BidDto getById(Long id);
    void deleteById(Long id);
    PlaceBidResult placeBid(Long auctionId, BigDecimal amount, String bidderEmail);
}
