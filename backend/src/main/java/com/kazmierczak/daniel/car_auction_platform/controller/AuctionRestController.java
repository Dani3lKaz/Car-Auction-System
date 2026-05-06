package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.dto.AuctionDto;
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
    public List<AuctionDto> getAll() {
        return auctionService.getAll();
    }

    @GetMapping("/{auctionId}")
    public AuctionDto getAuction(@PathVariable Long auctionId) {
        return auctionService.getById(auctionId);
    }

    @PostMapping
    public ResponseEntity<AuctionDto> addAuction(@RequestBody AuctionDto auctionDto) {
        auctionDto.setId(null);

        AuctionDto saved = auctionService.saveAuction(auctionDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping
    public AuctionDto updateAuction(@RequestBody AuctionDto auctionDto) {
        return auctionService.saveAuction(auctionDto);
    }

    @DeleteMapping("/{auctionId}")
    public String deleteAuction(@PathVariable Long auctionId) {
        auctionService.deleteById(auctionId);
        return "Deleted auction id - " + auctionId;
    }
}
