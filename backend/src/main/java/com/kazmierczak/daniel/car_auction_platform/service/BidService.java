package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.mapper.BidMapper;
import com.kazmierczak.daniel.car_auction_platform.models.dto.bid.BidDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.bid.CreateBidDTO;
import com.kazmierczak.daniel.car_auction_platform.models.entity.User;
import com.kazmierczak.daniel.car_auction_platform.exception.InvalidBidException;
import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.models.enums.AuctionStatus;
import com.kazmierczak.daniel.car_auction_platform.repository.AuctionRepository;
import com.kazmierczak.daniel.car_auction_platform.repository.BidRepository;
import com.kazmierczak.daniel.car_auction_platform.models.entity.Auction;
import com.kazmierczak.daniel.car_auction_platform.models.entity.Bid;
import com.kazmierczak.daniel.car_auction_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final BidMapper bidMapper;

    public List<BidDTO> getAll() {
        return bidRepository.findAll().stream()
                .map(bidMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BidDTO getById(Long id) {
        Optional<Bid> result = bidRepository.findById(id);

        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Bid with id " + id + " not found.");
        }

        return bidMapper.toDTO(result.get());
    }

    @Transactional
    public BidDTO saveBid(CreateBidDTO bidDto, User bidder) {
        if (bidder == null) {
            throw new InvalidBidException("User is required to place a bid.");
        }

        Auction auction = auctionRepository.findByIdForUpdate(bidDto.getAuctionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Auction with id " + bidDto.getAuctionId() + " not found."));

        if (auction.getStatus() != AuctionStatus.ACTIVE || LocalDateTime.now().isAfter(auction.getEndTime())) {
            throw new InvalidBidException("Auction is not active or has already ended - " + auction.getId());
        }

        BigDecimal amount = bidDto.getAmount();
        Bid topBid = bidRepository.findTopByAuctionIdOrderByAmountDesc(auction.getId()).orElse(null);

        if (topBid == null) {
            if (amount.compareTo(auction.getCurrentPrice()) < 0) {
                throw new InvalidBidException("First bid must be at least the starting price - " + auction.getCurrentPrice());
            }
        } else {
            if (topBid.getUser().getId().equals(bidder.getId())) {
                throw new InvalidBidException("You are already the highest bidder");
            }
            if (amount.subtract(auction.getCurrentPrice()).compareTo(auction.getMinIncrement()) < 0) {
                throw new InvalidBidException("Bid must be at least " + auction.getMinIncrement() +
                        " higher than the current price");
            }
        }

        moveFunds(bidder.getId(), amount, topBid);

        auction.setCurrentPrice(amount);
        auctionRepository.save(auction);

        Bid bid = bidMapper.toEntity(bidDto, auction, bidder);
        bid.setCreatedAt(LocalDateTime.now());
        return bidMapper.toDTO(bidRepository.save(bid));
    }

    private void moveFunds(Long bidderId, BigDecimal amount, Bid previousTopBid) {
        if (previousTopBid == null) {
            debit(bidderId, amount);
            return;
        }

        Long previousBidderId = previousTopBid.getUser().getId();
        BigDecimal refund = previousTopBid.getAmount();

        if (bidderId < previousBidderId) {
            debit(bidderId, amount);
            userRepository.credit(previousBidderId, refund);
        } else {
            userRepository.credit(previousBidderId, refund);
            debit(bidderId, amount);
        }
    }

    private void debit(Long userId, BigDecimal amount) {
        if (userRepository.debit(userId, amount) == 0) {
            throw new InvalidBidException("User with id " + userId + " has insufficient balance.");
        }
    }
}
