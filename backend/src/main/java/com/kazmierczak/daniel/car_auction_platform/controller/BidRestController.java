package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.dto.BidDto;
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
    public List<BidDto> getAll() {
        return bidService.getAll();
    }

    @GetMapping("/{bidId}")
    public BidDto getBid(@PathVariable Long bidId) {
        return bidService.getById(bidId);
    }

    @PostMapping
    public ResponseEntity<BidDto> addBid(@RequestBody BidDto bidDto) {
        bidDto.setId(null);

        BidDto saved = bidService.saveBid(bidDto);

        return  ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping
    public BidDto updateBid(@RequestBody BidDto bidDto) {
        return bidService.saveBid(bidDto);
    }

    @DeleteMapping("/{bidId}")
    public String deleteBid(@PathVariable Long bidId) {
        bidService.deleteById(bidId);
        return "Deleted bid id - " + bidId;
    }
}
