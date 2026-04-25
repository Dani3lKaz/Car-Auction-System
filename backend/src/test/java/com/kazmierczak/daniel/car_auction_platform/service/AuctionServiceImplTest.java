package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dto.AuctionDto;
import com.kazmierczak.daniel.car_auction_platform.entity.Auction;
import com.kazmierczak.daniel.car_auction_platform.entity.Vehicle;
import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.mapper.AuctionMapper;
import com.kazmierczak.daniel.car_auction_platform.repository.AuctionRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Auction Service Implementation Test")
class AuctionServiceImplTest {

    @Mock
    private AuctionRepository auctionRepository;

    @InjectMocks
    private AuctionServiceImpl auctionServiceImpl;

    @Test
    @DisplayName("Should return auction when auction exists")
    void shouldReturnAuctionWhenAuctionExists() {
        // given
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

        // when
        AuctionDto resultDto = auctionServiceImpl.getById(auctionId);

        // then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(expectedDto.getId());
        assertThat(resultDto.getStatus()).isEqualTo(expectedDto.getStatus());
        assertThat(resultDto.getStartPrice()).isEqualTo(expectedDto.getStartPrice());
        assertThat(resultDto.getMinIncrement()).isEqualTo(expectedDto.getMinIncrement());
    }

    @Test
    @DisplayName("Should throw exception when auction id not found")
    void shouldThrowExceptionWhenAuctionIdNotFound() {
        // given
        long nonExistentId = 99L;

        when(auctionRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> auctionServiceImpl.getById(nonExistentId));
    }

    @Test
    @DisplayName("Should successfully save auction and set current price to start price")
    void shouldSaveAuctionAndSetCurrentPrice() {
        // given
        BigDecimal startPrice = BigDecimal.valueOf(50000);
        AuctionDto inputDto = AuctionDto.builder()
                .startPrice(startPrice)
                .minIncrement(BigDecimal.valueOf(500))
                .status("ACTIVE")
                .build();

        Auction savedAuction = Auction.builder()
                .id(1L)
                .startPrice(startPrice)
                .currentPrice(startPrice)
                .minIncrement(BigDecimal.valueOf(500))
                .status("ACTIVE")
                .build();

        when(auctionRepository.save(any(Auction.class))).thenReturn(savedAuction);

        // when
        AuctionDto resultDto = auctionServiceImpl.saveAuction(inputDto);

        // then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(1L);
        assertThat(resultDto.getStartPrice()).isEqualTo(startPrice);
        assertThat(resultDto.getCurrentPrice()).isEqualTo(startPrice);

        verify(auctionRepository).save(any(Auction.class));
    }

    @Test
    @DisplayName("Should successfully delete auction when auction exists")
    void shouldDeleteAuctionWhenAuctionExists() {
        // given
        long auctionId = 1L;
        when(auctionRepository.existsById(auctionId)).thenReturn(true);

        // when
        auctionServiceImpl.deleteById(auctionId);

        // then
        verify(auctionRepository).existsById(auctionId);
        verify(auctionRepository).deleteById(auctionId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non existing auction")
    void shouldThrowExceptionWhenDeletingNonExistingAuction() {
        // given
        long nonExistentId = 99L;
        when(auctionRepository.existsById(nonExistentId)).thenReturn(false);

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> auctionServiceImpl.deleteById(nonExistentId));

        verify(auctionRepository, never()).deleteById(nonExistentId);
    }
}
