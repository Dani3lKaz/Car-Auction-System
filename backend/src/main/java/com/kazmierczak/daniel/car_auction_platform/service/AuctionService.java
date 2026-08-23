package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.mapper.AuctionMapper;
import com.kazmierczak.daniel.car_auction_platform.models.dto.auction.AuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.auction.CreateAuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.auction.SimpleAuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.repository.AuctionRepository;
import com.kazmierczak.daniel.car_auction_platform.models.entity.Auction;
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
    private final AuctionMapper auctionMapper;

    public List<SimpleAuctionDTO> getAll() {
        return auctionRepository.findAll().stream()
                .map(auctionMapper::toSimpleDTO)
                .collect(Collectors.toList());
    }

    public AuctionDTO getById(Long id) {
        Optional<Auction> result = auctionRepository.findById(id);
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Auction with id " + id + " not found.");
        }
        return auctionMapper.toDTO(result.get());
    }

    public List<AuctionDTO> getByStatus(String status) {
        return auctionRepository.findByStatus(status).stream()
                .map(auctionMapper::toDTO)
                .toList();
    }

    @Transactional
    public AuctionDTO saveAuction(CreateAuctionDTO auctionDto) {
        Auction auction = auctionMapper.toEntity(auctionDto);
        auction.setCurrentPrice(auction.getStartPrice());
        Auction savedAuction = auctionRepository.save(auction);
        return auctionMapper.toDTO(savedAuction);
    }
}