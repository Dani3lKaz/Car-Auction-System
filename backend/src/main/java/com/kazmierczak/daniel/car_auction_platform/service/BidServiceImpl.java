package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.exception.InvalidBidException;
import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.repository.AuctionRepository;
import com.kazmierczak.daniel.car_auction_platform.repository.BidRepository;
import com.kazmierczak.daniel.car_auction_platform.dto.BidDto;
import com.kazmierczak.daniel.car_auction_platform.entity.Auction;
import com.kazmierczak.daniel.car_auction_platform.entity.Bid;
import com.kazmierczak.daniel.car_auction_platform.mapper.BidMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;

    @Autowired
    public BidServiceImpl(BidRepository bidRepository, AuctionRepository auctionRepository) {
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
    }

    @Override
    public List<BidDto> getAll() {
        return bidRepository.findAll().stream()
                .map(BidMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BidDto getById(Long id) {
        Optional<Bid> result = bidRepository.findById(id);

        if (result.isPresent()) {
            return BidMapper.toDto(result.get());
        } else {
            throw new ResourceNotFoundException("Bid with id " + id + " not found.");
        }
    }

    @Override
    @Transactional
    public BidDto saveBid(BidDto bidDto) {
        Bid bid = BidMapper.toEntity(bidDto);
        Auction auction = bid.getAuction();

        if (auction == null) {
            throw new InvalidBidException("Auction is required to place a bid.");
        }

        Optional<Auction> auctionResult = auctionRepository.findById(auction.getId());
        if (auctionResult.isEmpty()) {
             throw new ResourceNotFoundException("Auction with id " + auction.getId() + " not found.");
        }
        
        Auction dbAuction = auctionResult.get();

        if (!"ACTIVE".equals(dbAuction.getStatus()) || LocalDateTime.now().isAfter(dbAuction.getEndTime())) {
            throw new InvalidBidException("Auction is not active or has already ended - " + dbAuction.getId());
        }

        if (bid.getAmount().subtract(dbAuction.getCurrentPrice()).compareTo(dbAuction.getMinIncrement()) < 0) {
            throw new InvalidBidException("Bid amount is too low");
        }
        dbAuction.setCurrentPrice(bid.getAmount());
        auctionRepository.save(dbAuction);

        bid.setAuction(dbAuction);
        Bid savedBid = bidRepository.save(bid);
        return BidMapper.toDto(savedBid);
    }

    @Override
    public void deleteById(Long id) {
        bidRepository.deleteById(id);
    }
}
