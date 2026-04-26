package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dto.AuctionDto;
import com.kazmierczak.daniel.car_auction_platform.dto.BidDto;
import com.kazmierczak.daniel.car_auction_platform.dto.UserDto;
import com.kazmierczak.daniel.car_auction_platform.entity.Auction;
import com.kazmierczak.daniel.car_auction_platform.entity.Bid;
import com.kazmierczak.daniel.car_auction_platform.entity.User;
import com.kazmierczak.daniel.car_auction_platform.exception.InvalidBidException;
import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.mapper.AuctionMapper;
import com.kazmierczak.daniel.car_auction_platform.mapper.BidMapper;
import com.kazmierczak.daniel.car_auction_platform.mapper.UserMapper;
import com.kazmierczak.daniel.car_auction_platform.repository.AuctionRepository;
import com.kazmierczak.daniel.car_auction_platform.repository.BidRepository;
import com.kazmierczak.daniel.car_auction_platform.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Bid Service Implementation Test")
public class BidServiceImplTest {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BidServiceImpl bidServiceImpl;

    @Test
    @DisplayName("Should return bid when bid exists")
    void shouldReturnBidWhenBidExists() {
        // given
        Long bidId = 1L;
        Bid bid = Bid.builder()
                .id(bidId)
                .user(new User())
                .auction(new Auction())
                .amount(BigDecimal.valueOf(100))
                .createdAt(LocalDateTime.now())
                .build();

        BidDto expectedDto = BidMapper.toDto(bid);
        when(bidRepository.findById(bidId)).thenReturn(Optional.of(bid));

        //when
        BidDto resultDto = bidServiceImpl.getById(bidId);

        //then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(expectedDto.getId());
        assertThat(resultDto.getUser()).isEqualTo(expectedDto.getUser());
        assertThat(resultDto.getAuction()).isEqualTo(expectedDto.getAuction());
        assertThat(resultDto.getAmount()).isEqualTo(expectedDto.getAmount());
        assertThat(resultDto.getCreatedAt()).isEqualTo(expectedDto.getCreatedAt());
    }

    @Test
    @DisplayName("Should throw exception when bid id not found")
    void shouldThrowExceptionWhenBidIdNotFound() {
        // given
        Long nonExistentId = 99L;
        when(bidRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> bidServiceImpl.getById(nonExistentId));
        assertThat(exception.getMessage()).isEqualTo("Bid with id " + nonExistentId + " not found.");
    }

    @Test
    @DisplayName("Should throw exception when auction is null")
    void  shouldThrowExceptionWhenAuctionIsNull() {
        //given
        BidDto bidDto = BidDto.builder().
                user(new UserDto())
                .build();

        //when & then
        InvalidBidException exception = assertThrows(InvalidBidException.class, () -> bidServiceImpl.saveBid(bidDto));
        assertThat(exception.getMessage()).isEqualTo("Auction is required to place a bid.");

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user is null")
    void  shouldThrowExceptionWhenUserIsNull() {
        //given
        BidDto bidDto = BidDto.builder()
                .auction(new AuctionDto())
                .build();

        //when & then
        InvalidBidException exception = assertThrows(InvalidBidException.class, () -> bidServiceImpl.saveBid(bidDto));
        assertThat(exception.getMessage()).isEqualTo("User is required to place a bid.");

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when auction does not exist")
    void  shouldThrowExceptionWhenAuctionDoesNotExist() {
        //given
        BidDto bidDto = BidDto.builder()
                .auction(AuctionDto.builder().id(99L).build())
                .user(new UserDto())
                .build();
        when(auctionRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> bidServiceImpl.saveBid(bidDto));
        assertThat(exception.getMessage()).isEqualTo("Auction with id " + 99L + " not found.");

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user does not exist")
    void  shouldThrowExceptionWhenUserDoesNotExist() {
        //given
        Auction auction = Auction.builder().id(1L).build();

        BidDto bidDto = BidDto.builder()
                .auction(AuctionMapper.toDto(auction))
                .user(UserDto.builder().id(99L).build())
                .build();

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> bidServiceImpl.saveBid(bidDto));
        assertThat(exception.getMessage()).isEqualTo("User with id " + 99L + " not found.");

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user is already the highest bidder")
    void  shouldThrowExceptionWhenUserIsAlreadyTheHighestBidder() {
        //given
        Auction auction = Auction.builder().id(1L).build();
        User user = User.builder().id(1L).build();
        Bid topBid = Bid.builder().auction(auction).user(user).build();
        BidDto bidDto = BidDto.builder()
                .auction(AuctionMapper.toDto(auction))
                .user(UserMapper.toDto(user))
                .build();
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.of(topBid));

        // when & then
        InvalidBidException exception = assertThrows(InvalidBidException.class, () -> bidServiceImpl.saveBid(bidDto));
        assertThat(exception.getMessage()).isEqualTo("You are already the highest bidder");

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user has insufficient balance")
    void  shouldThrowExceptionWhenUserHasInsufficientBalance() {
        //given
        Auction auction = Auction.builder().id(1L).build();
        User user = User.builder().id(1L).balance(BigDecimal.valueOf(100)).build();
        BidDto bidDto = BidDto.builder()
                .auction(AuctionMapper.toDto(auction))
                .user(UserMapper.toDto(user))
                .amount(BigDecimal.valueOf(1000))
                .build();
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.empty());

        // when & then
        InvalidBidException exception = assertThrows(InvalidBidException.class, () -> bidServiceImpl.saveBid(bidDto));
        assertThat(exception.getMessage()).isEqualTo("User with id " + 1L + " has insufficient balance.");

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when auction is not active")
    void  shouldThrowExceptionWhenAuctionIsNotActive() {
        //given
        Auction auction = Auction.builder().id(1L).status("FINISHED").build();
        User user = User.builder().id(1L).balance(BigDecimal.valueOf(1000)).build();
        BidDto bidDto = BidDto.builder()
                .auction(AuctionMapper.toDto(auction))
                .user(UserMapper.toDto(user))
                .amount(BigDecimal.valueOf(100))
                .build();
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.empty());

        // when & then
        InvalidBidException exception = assertThrows(InvalidBidException.class, () -> bidServiceImpl.saveBid(bidDto));
        assertThat(exception.getMessage()).isEqualTo("Auction is not active or has already ended - " + 1L);

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when auction has alreadey ended")
    void  shouldThrowExceptionWhenAuctionHasAlreadyEnded() {
        //given
        LocalDateTime endTime = LocalDateTime.now();
        endTime = endTime.minusDays(1);
        Auction auction = Auction.builder().id(1L).status("ACTIVE").endTime(endTime).build();
        User user = User.builder().id(1L).balance(BigDecimal.valueOf(1000)).build();
        BidDto bidDto = BidDto.builder()
                .auction(AuctionMapper.toDto(auction))
                .user(UserMapper.toDto(user))
                .amount(BigDecimal.valueOf(100))
                .build();
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.empty());

        // when & then
        InvalidBidException exception = assertThrows(InvalidBidException.class, () -> bidServiceImpl.saveBid(bidDto));
        assertThat(exception.getMessage()).isEqualTo("Auction is not active or has already ended - " + 1L);

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when first bid is too low")
    void   shouldThrowExceptionWhenFirstBidIsTooLow() {
        //given
        LocalDateTime endTime = LocalDateTime.now();
        endTime = endTime.plusDays(1);
        Auction auction = Auction.builder()
                .id(1L)
                .startPrice(BigDecimal.valueOf(1000))
                .currentPrice(BigDecimal.valueOf(1000))
                .status("ACTIVE")
                .endTime(endTime)
                .build();
        User user = User.builder().id(1L).balance(BigDecimal.valueOf(1000)).build();
        BidDto bidDto = BidDto.builder()
                .auction(AuctionMapper.toDto(auction))
                .user(UserMapper.toDto(user))
                .amount(BigDecimal.valueOf(100))
                .build();
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.empty());

        // when & then
        InvalidBidException exception = assertThrows(InvalidBidException.class, () -> bidServiceImpl.saveBid(bidDto));
        assertThat(exception.getMessage()).isEqualTo(
                "First bid must be at least the starting price - " + BigDecimal.valueOf(1000));

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when bid is too low")
    void   shouldThrowExceptionWhenBidIsTooLow() {
        //given
        LocalDateTime endTime = LocalDateTime.now();
        endTime = endTime.plusDays(1);
        Auction auction = Auction.builder()
                .id(1L)
                .startPrice(BigDecimal.valueOf(1000))
                .currentPrice(BigDecimal.valueOf(1100))
                .minIncrement(BigDecimal.valueOf(100))
                .status("ACTIVE")
                .endTime(endTime)
                .build();
        User user = User.builder().id(1L).balance(BigDecimal.valueOf(1000)).build();
        BidDto bidDto = BidDto.builder()
                .auction(AuctionMapper.toDto(auction))
                .user(UserMapper.toDto(user))
                .amount(BigDecimal.valueOf(1000))
                .build();
        User topBidUser = User.builder().id(2L).build();
        Bid topBid = Bid.builder().user(topBidUser).build();
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.of(topBid));

        // when & then
        InvalidBidException exception = assertThrows(InvalidBidException.class, () -> bidServiceImpl.saveBid(bidDto));
        assertThat(exception.getMessage()).isEqualTo("Bid must be at least " + auction.getMinIncrement() +
                " higher than the current price");

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should save bid successfully when no previous bidder")
    void  shouldSaveBidSuccessfullyWhenNoPreviousBidder() {
        //given
        LocalDateTime endTime = LocalDateTime.now();
        endTime = endTime.plusDays(1);
        Auction auction = Auction.builder()
                .id(1L)
                .startPrice(BigDecimal.valueOf(1000))
                .currentPrice(BigDecimal.valueOf(1000))
                .status("ACTIVE")
                .endTime(endTime)
                .build();
        User user = User.builder().id(1L).balance(BigDecimal.valueOf(1000)).build();
        BidDto bidDto = BidDto.builder()
                .auction(AuctionMapper.toDto(auction))
                .user(UserMapper.toDto(user))
                .amount(BigDecimal.valueOf(1000))
                .build();

        LocalDateTime now = LocalDateTime.now();

        Bid savedBid = Bid.builder()
                .id(1L)
                .auction(auction)
                .user(user)
                .amount(BigDecimal.valueOf(1000))
                .createdAt(now)
                .build();

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.empty());
        when(bidRepository.save(any())).thenReturn(savedBid);

        // when
        BidDto resultDto = bidServiceImpl.saveBid(bidDto);

        //then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getAmount()).isEqualTo(bidDto.getAmount());
        assertThat(resultDto.getCreatedAt()).isNotNull();
        assertThat(resultDto.getAuction().getId()).isEqualTo(bidDto.getAuction().getId());
        assertThat(resultDto.getUser().getId()).isEqualTo(bidDto.getUser().getId());
        assertThat(resultDto.getUser().getBalance()).isEqualTo(BigDecimal.valueOf(0));


        verify(bidRepository).save(any(Bid.class));
        verify(userRepository).save(any(User.class));
        verify(auctionRepository).save(any(Auction.class));
    }

    @Test
    @DisplayName("Should refund previous bidder when top bidder exists")
    void  shouldRefundPreviousBidderWhenTopBidderExists() {
        //given
        LocalDateTime endTime = LocalDateTime.now();
        endTime = endTime.plusDays(1);
        Auction auction = Auction.builder()
                .id(1L)
                .startPrice(BigDecimal.valueOf(1000))
                .currentPrice(BigDecimal.valueOf(1000))
                .minIncrement(BigDecimal.valueOf(100))
                .status("ACTIVE")
                .endTime(endTime)
                .build();
        User user = User.builder().id(1L).balance(BigDecimal.valueOf(1100)).build();
        BidDto bidDto = BidDto.builder()
                .auction(AuctionMapper.toDto(auction))
                .user(UserMapper.toDto(user))
                .amount(BigDecimal.valueOf(1100))
                .build();

        LocalDateTime now = LocalDateTime.now();

        Bid savedBid = Bid.builder()
                .id(1L)
                .auction(auction)
                .user(user)
                .amount(BigDecimal.valueOf(1100))
                .createdAt(now)
                .build();

        User topBidUser = User.builder().id(2L).balance(BigDecimal.valueOf(0)).build();
        Bid topBid = Bid.builder().user(topBidUser).amount(BigDecimal.valueOf(1000)).build();

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.of(topBid));
        when(bidRepository.save(any())).thenReturn(savedBid);

        // when
        BidDto resultDto = bidServiceImpl.saveBid(bidDto);

        //then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getAmount()).isEqualTo(bidDto.getAmount());
        assertThat(resultDto.getCreatedAt()).isNotNull();
        assertThat(resultDto.getAuction().getId()).isEqualTo(bidDto.getAuction().getId());
        assertThat(resultDto.getUser().getId()).isEqualTo(bidDto.getUser().getId());
        assertThat(resultDto.getUser().getBalance()).isEqualTo(BigDecimal.valueOf(0));
        assertThat(topBidUser.getBalance()).isEqualTo(BigDecimal.valueOf(1000));

        verify(bidRepository).save(any(Bid.class));
        verify(userRepository, times(2)).save(any(User.class));
        verify(auctionRepository).save(any(Auction.class));
    }

    @Test
    @DisplayName("Should successfully delete bid when bid exists")
    void shouldDeleteBidWhenBidExists() {
        // given
        long bidId = 1L;
        when(bidRepository.existsById(bidId)).thenReturn(true);

        // when
        bidServiceImpl.deleteById(bidId);

        // then
        verify(bidRepository).existsById(bidId);
        verify(bidRepository).deleteById(bidId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non existing bid")
    void shouldThrowExceptionWhenDeletingNonExistingBid() {
        // given
        long nonExistentId = 99L;
        when(bidRepository.existsById(nonExistentId)).thenReturn(false);

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> bidServiceImpl.deleteById(nonExistentId));

        verify(bidRepository, never()).deleteById(nonExistentId);
    }
}
