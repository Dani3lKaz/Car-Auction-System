package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.repository.AuctionRepository;
import com.kazmierczak.daniel.car_auction_platform.dto.AuctionDto;
import com.kazmierczak.daniel.car_auction_platform.entity.Auction;
import com.kazmierczak.daniel.car_auction_platform.mapper.AuctionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;

    @Override
    public List<AuctionDto> getAll() {
        return auctionRepository.findAll().stream()
                .map(AuctionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuctionDto getById(Long id) {
        Optional<Auction> result = auctionRepository.findById(id);
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Auction with id " + id + " not found.");
        }
        return AuctionMapper.toDto(result.get());
    }

    @Override
    public List<AuctionDto> getByStatus(String status) {
        return auctionRepository.findByStatus(status).stream()
                .map(AuctionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public AuctionDto saveAuction(AuctionDto auctionDto) {
        Auction auction = AuctionMapper.toEntity(auctionDto);
        if (auction.getId() == null || auction.getCurrentPrice() == null) {
            auction.setCurrentPrice(auction.getStartPrice());
        }
        Auction savedAuction = auctionRepository.save(auction);
        return AuctionMapper.toDto(savedAuction);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if(!auctionRepository.existsById(id)){
            throw new ResourceNotFoundException("Cannot delete. Auction with id " + id + " not found.");
        }
        auctionRepository.deleteById(id);
    }
}
