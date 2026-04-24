package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.entity.User;
import com.kazmierczak.daniel.car_auction_platform.exception.InvalidBidException;
import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.repository.AuctionRepository;
import com.kazmierczak.daniel.car_auction_platform.repository.BidRepository;
import com.kazmierczak.daniel.car_auction_platform.dto.BidDto;
import com.kazmierczak.daniel.car_auction_platform.entity.Auction;
import com.kazmierczak.daniel.car_auction_platform.entity.Bid;
import com.kazmierczak.daniel.car_auction_platform.mapper.BidMapper;
import com.kazmierczak.daniel.car_auction_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    @Override
    public List<BidDto> getAll() {
        return bidRepository.findAll().stream()
                .map(BidMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BidDto getById(Long id) {
        Optional<Bid> result = bidRepository.findById(id);

        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Bid with id " + id + " not found.");
        }

        return BidMapper.toDto(result.get());
    }

    @Override
    @Transactional
    public BidDto saveBid(BidDto bidDto) {
        Bid bid = BidMapper.toEntity(bidDto);
        Auction auction = bid.getAuction();
        User user = bid.getUser();

        if (auction == null) {
            throw new InvalidBidException("Auction is required to place a bid.");
        }

        if (user == null) {
            throw new InvalidBidException("User is required to place a bid.");
        }

        Optional<Auction> auctionResult = auctionRepository.findById(auction.getId());
        if (auctionResult.isEmpty()) {
             throw new ResourceNotFoundException("Auction with id " + auction.getId() + " not found.");
        }

        Optional<User> userResult = userRepository.findById(user.getId());
        if (userResult.isEmpty()) {
            throw new InvalidBidException("User with id " + user.getId() + " not found.");
        }
        
        Auction dbAuction = auctionResult.get();
        User dbUser = userResult.get();
        Optional<Bid> topBidResult = bidRepository.findTopByAuctionIdOrderByAmountDesc(dbAuction.getId());
        User topBidder = null;
        Bid topBid = null;

        if(topBidResult.isPresent()) {
            topBid = topBidResult.get();
            if(!topBid.getUser().getId().equals(dbUser.getId())) {
                topBidder = topBid.getUser();
            }else{
                throw  new InvalidBidException("You are already the highest bidder");
            }
        }

        if (dbUser.getBalance().compareTo(bid.getAmount()) < 0) {
            throw new InvalidBidException("User with id " + dbUser.getId() + " has not enough balance.");
        }

        if (!dbAuction.getStatus().equals("ACTIVE") || LocalDateTime.now().isAfter(dbAuction.getEndTime())) {
            throw new InvalidBidException("Auction is not active or has already ended - " + dbAuction.getId());
        }

        if (topBid == null) {
            if (bid.getAmount().compareTo(dbAuction.getCurrentPrice()) < 0) {
                throw new InvalidBidException("First bid must be at least the starting price - " + dbAuction.getCurrentPrice());
            }
        } else {
            if (bid.getAmount().subtract(dbAuction.getCurrentPrice()).compareTo(dbAuction.getMinIncrement()) < 0) {
                throw new InvalidBidException("Bid must be at least " + dbAuction.getMinIncrement() +
                        " higher than the current price");
            }
        }

        dbAuction.setCurrentPrice(bid.getAmount());
        auctionRepository.save(dbAuction);

        dbUser.setBalance(dbUser.getBalance().subtract(bid.getAmount()));
        userRepository.save(dbUser);

        if(topBidder != null) {
            topBidder.setBalance(topBidder.getBalance().add(topBid.getAmount()));
            userRepository.save(topBidder);
        }

        bid.setCreatedAt(LocalDateTime.now());
        Bid savedBid = bidRepository.save(bid);
        return BidMapper.toDto(savedBid);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if(!bidRepository.existsById(id)){
            throw new ResourceNotFoundException("Cannot delete. Bid with id " + id + " not found.");
        }
        bidRepository.deleteById(id);
    }
}
