package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.exception.VehicleVinAlreadyExistsException;
import com.kazmierczak.daniel.car_auction_platform.mapper.AuctionMapper;
import com.kazmierczak.daniel.car_auction_platform.model.dto.auction.AuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.model.dto.auction.CreateAuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.model.dto.auction.SimpleAuctionDTO;
import com.kazmierczak.daniel.car_auction_platform.model.entity.User;
import com.kazmierczak.daniel.car_auction_platform.model.enums.AuctionStatus;
import com.kazmierczak.daniel.car_auction_platform.repository.AuctionRepository;
import com.kazmierczak.daniel.car_auction_platform.repository.VehicleRepository;
import com.kazmierczak.daniel.car_auction_platform.model.entity.Auction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final AuctionMapper auctionMapper;
    private final VehicleRepository vehicleRepository;
    private final FileStorageService fileStorageService;

    public List<SimpleAuctionDTO> getAll() {
        return auctionRepository.findAll().stream()
                .map(auctionMapper::toSimpleDTO)
                .collect(Collectors.toList());
    }

    public AuctionDTO getById(Long id) {
        Optional<Auction> result = auctionRepository.findById(id);
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Auction with id " + id + " not found.");
        }
        return auctionMapper.toDTO(result.get());
    }

    @Transactional
    public AuctionDTO saveAuction(CreateAuctionDTO auctionDto, MultipartFile image, User seller) {
        Auction auction = auctionMapper.toEntity(auctionDto);
        String vin = auction.getVehicle().getVin();
        if (vehicleRepository.findByVin(vin).isPresent()) {
            throw new VehicleVinAlreadyExistsException("Vehicle with vin " + vin + " already exists.");
        }
        auction.setUser(seller);
        auction.setCurrentPrice(auction.getStartPrice());
        auction.setCreateTime(LocalDateTime.now());
        auction.setStatus(AuctionStatus.ACTIVE);
        if (image != null && !image.isEmpty()) {
            auction.getVehicle().setImage(fileStorageService.storeImage(image));
        }
        try {
            Auction savedAuction = auctionRepository.save(auction);
            return auctionMapper.toDTO(savedAuction);
        } catch (RuntimeException e) {
            fileStorageService.delete(auction.getVehicle().getImage());
            throw e;
        }
    }
}