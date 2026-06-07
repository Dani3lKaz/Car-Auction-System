package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dto.AuctionDto;
import com.kazmierczak.daniel.car_auction_platform.dto.VehicleDto;
import com.kazmierczak.daniel.car_auction_platform.entity.Auction;
import com.kazmierczak.daniel.car_auction_platform.entity.User;
import com.kazmierczak.daniel.car_auction_platform.entity.Vehicle;
import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.exception.VehicleVinAlreadyExistsException;
import com.kazmierczak.daniel.car_auction_platform.mapper.AuctionMapper;
import com.kazmierczak.daniel.car_auction_platform.mapper.VehicleMapper;
import com.kazmierczak.daniel.car_auction_platform.repository.AuctionRepository;
import com.kazmierczak.daniel.car_auction_platform.repository.UserRepository;
import com.kazmierczak.daniel.car_auction_platform.repository.VehicleRepository;
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
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    @Override
    public List<AuctionDto> getAll() {
        return auctionRepository.findAll().stream()
                .map(AuctionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuctionDto getById(Long id) {
        Optional<Auction> result = auctionRepository.findById(id);
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Auction with id " + id + " not found.");
        }
        return AuctionMapper.toDto(result.get());
    }

    @Override
    public List<AuctionDto> getByStatus(String status) {
        closeExpired(auctionRepository.findAll());
        return auctionRepository.findByStatus(status).stream()
                .map(AuctionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public AuctionDto createAuction(AuctionDto auctionDto, String sellerEmail) {
        validateCreateRequest(auctionDto);

        User seller = userRepository.findByEmail(sellerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + sellerEmail + " not found."));

        VehicleDto vehicleDto = auctionDto.getVehicle();
        vehicleDto.setId(null);
        Vehicle vehicle = VehicleMapper.toEntity(vehicleDto);

        if (vehicleRepository.findByVin(vehicle.getVin()).isPresent()) {
            throw new VehicleVinAlreadyExistsException("Vehicle with vin " + vehicle.getVin() + " already exists.");
        }

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        String status = auctionDto.getStatus() != null ? auctionDto.getStatus() : "ACTIVE";
        Auction auction = Auction.builder()
                .vehicle(savedVehicle)
                .seller(seller)
                .startPrice(auctionDto.getStartPrice())
                .currentPrice(auctionDto.getStartPrice())
                .minIncrement(auctionDto.getMinIncrement())
                .endTime(auctionDto.getEndTime())
                .status(status)
                .build();

        Auction savedAuction = auctionRepository.save(auction);
        return AuctionMapper.toDto(savedAuction);
    }

    @Override
    @Transactional
    public AuctionDto updateAuction(AuctionDto auctionDto) {
        if (auctionDto.getId() == null) {
            throw new IllegalArgumentException("Auction id is required for update.");
        }

        Auction existing = auctionRepository.findById(auctionDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Auction with id " + auctionDto.getId() + " not found."));

        existing.setStartPrice(auctionDto.getStartPrice());
        existing.setMinIncrement(auctionDto.getMinIncrement());
        existing.setEndTime(auctionDto.getEndTime());
        if (auctionDto.getStatus() != null) {
            existing.setStatus(auctionDto.getStatus());
        }
        if (auctionDto.getCurrentPrice() != null) {
            existing.setCurrentPrice(auctionDto.getCurrentPrice());
        }

        Auction savedAuction = auctionRepository.save(existing);
        return AuctionMapper.toDto(savedAuction);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!auctionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Auction with id " + id + " not found.");
        }
        auctionRepository.deleteById(id);
    }

    private void validateCreateRequest(AuctionDto auctionDto) {
        if (auctionDto == null || auctionDto.getVehicle() == null) {
            throw new IllegalArgumentException("Vehicle data is required.");
        }

        VehicleDto vehicle = auctionDto.getVehicle();
        if (isBlank(vehicle.getBrand()) || isBlank(vehicle.getModel()) || vehicle.getYear() == null
                || isBlank(vehicle.getFuelType()) || vehicle.getEngineCapacity() == null
                || isBlank(vehicle.getVin())) {
            throw new IllegalArgumentException("Complete vehicle data is required.");
        }

        if (auctionDto.getStartPrice() == null || auctionDto.getStartPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Start price must be greater than zero.");
        }

        if (auctionDto.getMinIncrement() == null || auctionDto.getMinIncrement().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Minimum bid increment must be greater than zero.");
        }

        if (auctionDto.getEndTime() == null || !auctionDto.getEndTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Auction end time must be in the future.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private List<Auction> closeExpired(List<Auction> auctions) {
        LocalDateTime now = LocalDateTime.now();
        List<Auction> toClose = auctions.stream()
                .filter(a -> "ACTIVE".equals(a.getStatus()) && a.getEndTime() != null
                        && now.isAfter(a.getEndTime()))
                .toList();

        if (!toClose.isEmpty()) {
            toClose.forEach(a -> a.setStatus("ENDED"));
            auctionRepository.saveAll(toClose);
        }
        return auctions;
}
}
