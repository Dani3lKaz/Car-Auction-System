package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.models.dto.auction.AuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.auction.CreateAuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.auction.SimpleAuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.service.AuctionService;
import com.kazmierczak.daniel.car_auction_platform.models.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuctionDTO> addAuction(@Valid @RequestPart("auction") CreateAuctionDTO auctionDto,
                                                 @RequestPart(value = "image", required = false) MultipartFile image,
                                                 @AuthenticationPrincipal User user) {
        AuctionDTO saved = auctionService.saveAuction(auctionDto, image, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
