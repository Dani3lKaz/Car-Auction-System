package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.repository.AuctionRepository;
import com.kazmierczak.daniel.car_auction_platform.models.dto.AuctionDto;
import com.kazmierczak.daniel.car_auction_platform.models.entity.Auction;
import com.kazmierczak.daniel.car_auction_platform.mapper.AuctionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;

    public List<AuctionDto> getAll() {
        return auctionRepository.findAll().stream()
                .map(AuctionMapper::toDto)
                .collect(Collectors.toList());
    }

    public AuctionDto getById(Long id) {
        Optional<Auction> result = auctionRepository.findById(id);
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Auction with id " + id + " not found.");
        }
        return AuctionMapper.toDto(result.get());
    }

    public List<AuctionDto> getByStatus(String status) {
        return auctionRepository.findByStatus(status).stream()
                .map(AuctionMapper::toDto)
                .toList();
    }

    @Transactional
    public AuctionDto saveAuction(AuctionDto auctionDto) {
        Auction auction = AuctionMapper.toEntity(auctionDto);
        auction.setCurrentPrice(auction.getStartPrice());
        Auction savedAuction = auctionRepository.save(auction);
        return AuctionMapper.toDto(savedAuction);
    }

    @Transactional
    public void deleteById(Long id) {
        if(!auctionRepository.existsById(id)){
            throw new ResourceNotFoundException("Cannot delete. Auction with id " + id + " not found.");
        }
        auctionRepository.deleteById(id);
    }
}