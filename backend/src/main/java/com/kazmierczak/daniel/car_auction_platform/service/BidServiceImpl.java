package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.entity.User;
import com.kazmierczak.daniel.car_auction_platform.exception.InvalidBidException;
import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.repository.AuctionRepository;
import com.kazmierczak.daniel.car_auction_platform.repository.BidRepository;
import com.kazmierczak.daniel.car_auction_platform.dto.BidDto;
import com.kazmierczak.daniel.car_auction_platform.dto.PlaceBidResult;
import com.kazmierczak.daniel.car_auction_platform.entity.Auction;
import com.kazmierczak.daniel.car_auction_platform.entity.Bid;
import com.kazmierczak.daniel.car_auction_platform.mapper.BidMapper;
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
    public void deleteById(Long id) {
        if(!bidRepository.existsById(id)){
            throw new ResourceNotFoundException("Cannot delete. Bid with id " + id + " not found.");
        }
        bidRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PlaceBidResult placeBid(Long auctionId, BigDecimal amount, String bidderEmail) {
        User dbUser = userRepository.findByEmail(bidderEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        Auction dbAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction with id " + auctionId + " not found."));

        if (dbAuction.getSeller().getId().equals(dbUser.getId())) {
            throw new InvalidBidException("Sprzedawca nie może licytować własnej aukcji.");
        }

        Optional<Bid> topBidResult = bidRepository.findTopByAuctionIdOrderByAmountDesc(dbAuction.getId());
        User topBidder = null;
        Bid topBid = null;

        if (topBidResult.isPresent()) {
            topBid = topBidResult.get();
            if (topBid.getUser().getId().equals(dbUser.getId())) {
                throw new InvalidBidException("Jesteś już najwyższym licytantem.");
            }
            topBidder = topBid.getUser();
        }

        if (dbUser.getBalance().compareTo(amount) < 0) {
            throw new InvalidBidException("Niewystarczające saldo konta.");
        }

        if (!dbAuction.getStatus().equals("ACTIVE") || LocalDateTime.now().isAfter(dbAuction.getEndTime())) {
            throw new InvalidBidException("Aukcja nie jest aktywna lub już się zakończyła.");
        }

        if (topBid == null) {
            if (amount.compareTo(dbAuction.getCurrentPrice()) < 0) {
                throw new InvalidBidException("Pierwsza oferta musi wynosić co najmniej " + dbAuction.getCurrentPrice());
            }
        } else {
            if (amount.subtract(dbAuction.getCurrentPrice()).compareTo(dbAuction.getMinIncrement()) < 0) {
                throw new InvalidBidException("Oferta musi być co najmniej " + dbAuction.getMinIncrement() +
                        " wyższa od aktualnej ceny.");
            }
        }

        dbAuction.setCurrentPrice(amount);
        auctionRepository.save(dbAuction);

        dbUser.setBalance(dbUser.getBalance().subtract(amount));
        userRepository.save(dbUser);

        String outbidUserEmail = null;
        BigDecimal outbidAmount = null;

        if (topBidder != null) {
            topBidder.setBalance(topBidder.getBalance().add(topBid.getAmount()));
            userRepository.save(topBidder);
            outbidUserEmail = topBidder.getEmail();
            outbidAmount = topBid.getAmount();
        }

        Bid bid = Bid.builder()
                .auction(dbAuction)
                .user(dbUser)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .build();

        Bid savedBid = bidRepository.save(bid);
        BidDto savedBidDto = BidMapper.toDto(savedBid);

        return PlaceBidResult.builder()
                .savedBid(savedBidDto)
                .outbidUserEmail(outbidUserEmail)
                .outbidAmount(outbidAmount)
                .build();
    }
}
