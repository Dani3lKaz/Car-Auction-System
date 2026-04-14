package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dto.BidDto;

import java.util.List;

public interface BidService {
    List<BidDto> getAll();
    BidDto getById(Long id);
    BidDto saveBid(BidDto bidDto);
    void deleteById(Long id);
}
