package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dto.AuctionDto;
import com.kazmierczak.daniel.car_auction_platform.dto.VehicleDto;
import com.kazmierczak.daniel.car_auction_platform.entity.Auction;
import com.kazmierczak.daniel.car_auction_platform.entity.Role;
import com.kazmierczak.daniel.car_auction_platform.entity.User;
import com.kazmierczak.daniel.car_auction_platform.entity.Vehicle;
import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.mapper.AuctionMapper;
import com.kazmierczak.daniel.car_auction_platform.repository.AuctionRepository;
import com.kazmierczak.daniel.car_auction_platform.repository.UserRepository;
import com.kazmierczak.daniel.car_auction_platform.repository.VehicleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Auction Service Implementation Test")
class AuctionServiceImplTest {

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuctionServiceImpl auctionServiceImpl;

    @Test
    @DisplayName("Should return auction when auction exists")
    void shouldReturnAuctionWhenAuctionExists() {
        long auctionId = 1L;
        Auction auction = Auction.builder()
                .id(auctionId)
                .vehicle(new Vehicle())
                .startPrice(BigDecimal.valueOf(10000))
                .currentPrice(BigDecimal.valueOf(10000))
                .minIncrement(BigDecimal.valueOf(500))
                .endTime(LocalDateTime.now().plusDays(7))
                .status("ACTIVE")
                .build();
        AuctionDto expectedDto = AuctionMapper.toDto(auction);
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        AuctionDto resultDto = auctionServiceImpl.getById(auctionId);

        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(expectedDto.getId());
        assertThat(resultDto.getStatus()).isEqualTo(expectedDto.getStatus());
        assertThat(resultDto.getStartPrice()).isEqualTo(expectedDto.getStartPrice());
        assertThat(resultDto.getMinIncrement()).isEqualTo(expectedDto.getMinIncrement());
    }

    @Test
    @DisplayName("Should throw exception when auction id not found")
    void shouldThrowExceptionWhenAuctionIdNotFound() {
        long nonExistentId = 99L;

        when(auctionRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> auctionServiceImpl.getById(nonExistentId));
    }

    @Test
    @DisplayName("Should create auction with vehicle and seller")
    void shouldCreateAuctionWithVehicleAndSeller() {
        BigDecimal startPrice = BigDecimal.valueOf(50000);
        LocalDateTime endTime = LocalDateTime.now().plusDays(5);
        String sellerEmail = "seller@example.com";

        VehicleDto vehicleDto = VehicleDto.builder()
                .brand("BMW")
                .model("M3")
                .year(2021)
                .fuelType("Petrol")
                .engineCapacity(2998)
                .vin("WBA1234567890ABC")
                .build();

        AuctionDto inputDto = AuctionDto.builder()
                .vehicle(vehicleDto)
                .startPrice(startPrice)
                .minIncrement(BigDecimal.valueOf(500))
                .endTime(endTime)
                .build();

        User seller = User.builder()
                .id(2L)
                .email(sellerEmail)
                .role(Role.SELLER)
                .build();

        Vehicle savedVehicle = Vehicle.builder().id(10L).vin(vehicleDto.getVin()).build();
        Auction savedAuction = Auction.builder()
                .id(1L)
                .vehicle(savedVehicle)
                .seller(seller)
                .startPrice(startPrice)
                .currentPrice(startPrice)
                .minIncrement(BigDecimal.valueOf(500))
                .endTime(endTime)
                .status("ACTIVE")
                .build();

        when(userRepository.findByEmail(sellerEmail)).thenReturn(Optional.of(seller));
        when(vehicleRepository.findByVin(vehicleDto.getVin())).thenReturn(Optional.empty());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(savedVehicle);
        when(auctionRepository.save(any(Auction.class))).thenReturn(savedAuction);

        AuctionDto resultDto = auctionServiceImpl.createAuction(inputDto, sellerEmail);

        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(1L);
        assertThat(resultDto.getStartPrice()).isEqualTo(startPrice);
        assertThat(resultDto.getCurrentPrice()).isEqualTo(startPrice);
        assertThat(resultDto.getSeller().getEmail()).isEqualTo(sellerEmail);

        verify(vehicleRepository).save(any(Vehicle.class));
        verify(auctionRepository).save(any(Auction.class));
    }

    @Test
    @DisplayName("Should successfully delete auction when auction exists")
    void shouldDeleteAuctionWhenAuctionExists() {
        long auctionId = 1L;
        when(auctionRepository.existsById(auctionId)).thenReturn(true);

        auctionServiceImpl.deleteById(auctionId);

        verify(auctionRepository).existsById(auctionId);
        verify(auctionRepository).deleteById(auctionId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non existing auction")
    void shouldThrowExceptionWhenDeletingNonExistingAuction() {
        long nonExistentId = 99L;
        when(auctionRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> auctionServiceImpl.deleteById(nonExistentId));

        verify(auctionRepository, never()).deleteById(nonExistentId);
    }
}
