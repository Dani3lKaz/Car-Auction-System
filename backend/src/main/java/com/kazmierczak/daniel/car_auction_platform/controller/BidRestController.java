package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.dto.BidDto;
import com.kazmierczak.daniel.car_auction_platform.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
public class BidRestController {

    private final BidService bidService;

    @Autowired
    public BidRestController(BidService bidService) {
        this.bidService = bidService;
    }

    @GetMapping
    public List<BidDto> getAll() {
        return bidService.getAll();
    }

    @GetMapping("/{bidId}")
    public BidDto getBid(@PathVariable Long bidId) {
        return bidService.getById(bidId);
    }

    @GetMapping("/auction/{auctionId}")
    public List<BidDto> getBidsByAuctionId(@PathVariable Long auctionId) {
        return bidService.getBidsByAuctionId(auctionId);
    }

    @DeleteMapping("/{bidId}")
    public String deleteBid(@PathVariable Long bidId) {
        bidService.deleteById(bidId);
        return "Deleted bid id - " + bidId;
    }
}

