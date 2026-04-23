package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.repository.AuctionRepository;
import com.kazmierczak.daniel.car_auction_platform.dto.AuctionDto;
import com.kazmierczak.daniel.car_auction_platform.entity.Auction;
import com.kazmierczak.daniel.car_auction_platform.mapper.AuctionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;

    @Autowired
    public AuctionServiceImpl(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    @Override
    public List<AuctionDto> getAll() {
        return auctionRepository.findAll().stream()
                .map(AuctionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuctionDto getById(Long id) {
        Optional<Auction> result = auctionRepository.findById(id);
        if (result.isPresent()) {
            return AuctionMapper.toDto(result.get());
        } else {
            throw new ResourceNotFoundException("Auction with id " + id + " not found.");
        }
    }

    @Override
    public AuctionDto saveAuction(AuctionDto auctionDto) {
        Auction auction = AuctionMapper.toEntity(auctionDto);
        Auction savedAuction = auctionRepository.save(auction);
        return AuctionMapper.toDto(savedAuction);
    }

    @Override
    public void deleteById(Long id) {
        auctionRepository.deleteById(id);
    }
}
