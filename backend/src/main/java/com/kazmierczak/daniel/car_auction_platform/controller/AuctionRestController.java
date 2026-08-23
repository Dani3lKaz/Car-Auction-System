package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.models.dto.auction.AuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.auction.CreateAuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.auction.SimpleAuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auctions")
public class AuctionRestController {

    private final AuctionService auctionService;

    @Autowired
    public AuctionRestController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping
    public List<SimpleAuctionDTO> getAll() {
        return auctionService.getAll();
    }

    @GetMapping("/{auctionId}")
    public AuctionDTO getAuction(@PathVariable Long auctionId) {
        return auctionService.getById(auctionId);
    }

    @PostMapping
    public ResponseEntity<AuctionDTO> addAuction(@RequestBody CreateAuctionDTO auctionDto) {
        AuctionDTO saved = auctionService.saveAuction(auctionDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
