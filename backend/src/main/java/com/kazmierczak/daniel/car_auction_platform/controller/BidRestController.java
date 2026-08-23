package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.models.dto.bid.BidDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.bid.CreateBidDTO;
import com.kazmierczak.daniel.car_auction_platform.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<BidDTO> getAll() {
        return bidService.getAll();
    }

    @GetMapping("/{bidId}")
    public BidDTO getBid(@PathVariable Long bidId) {
        return bidService.getById(bidId);
    }

    @PostMapping
    public ResponseEntity<BidDTO> addBid(@RequestBody CreateBidDTO bidDto) {

        BidDTO saved = bidService.saveBid(bidDto);

        return  ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
