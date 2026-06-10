package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dto.BidDto;
import com.kazmierczak.daniel.car_auction_platform.entity.Auction;
import com.kazmierczak.daniel.car_auction_platform.entity.Bid;
import com.kazmierczak.daniel.car_auction_platform.entity.User;
import com.kazmierczak.daniel.car_auction_platform.exception.InvalidBidException;
import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.mapper.BidMapper;
import com.kazmierczak.daniel.car_auction_platform.repository.AuctionRepository;
import com.kazmierczak.daniel.car_auction_platform.repository.BidRepository;
import com.kazmierczak.daniel.car_auction_platform.repository.UserRepository;
import com.kazmierczak.daniel.car_auction_platform.dto.PlaceBidResult;
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

    // ── placeBid tests ──

    @Test
    @DisplayName("Should throw exception when user not found by email")
    void shouldThrowExceptionWhenUserNotFoundByEmail() {
        // given
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> bidServiceImpl.placeBid(1L, BigDecimal.valueOf(100), "unknown@test.com"));
        assertThat(exception.getMessage()).isEqualTo("User not found.");

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when auction does not exist")
    void shouldThrowExceptionWhenAuctionDoesNotExist() {
        // given
        User user = User.builder().id(1L).email("user@test.com").build();
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(auctionRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> bidServiceImpl.placeBid(99L, BigDecimal.valueOf(100), "user@test.com"));
        assertThat(exception.getMessage()).isEqualTo("Auction with id 99 not found.");

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when seller bids on own auction")
    void shouldThrowExceptionWhenSellerBidsOnOwnAuction() {
        // given
        User seller = User.builder().id(1L).email("seller@test.com").build();
        Auction auction = Auction.builder().id(1L).seller(seller).build();
        when(userRepository.findByEmail("seller@test.com")).thenReturn(Optional.of(seller));
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        // when & then
        InvalidBidException exception = assertThrows(InvalidBidException.class,
                () -> bidServiceImpl.placeBid(1L, BigDecimal.valueOf(100), "seller@test.com"));
        assertThat(exception.getMessage()).isEqualTo("Sprzedawca nie może licytować własnej aukcji.");

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user is already the highest bidder")
    void shouldThrowExceptionWhenUserIsAlreadyTheHighestBidder() {
        // given
        User seller = User.builder().id(10L).build();
        User bidder = User.builder().id(1L).email("bidder@test.com").build();
        Auction auction = Auction.builder().id(1L).seller(seller).build();
        Bid topBid = Bid.builder().auction(auction).user(bidder).build();

        when(userRepository.findByEmail("bidder@test.com")).thenReturn(Optional.of(bidder));
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.of(topBid));

        // when & then
        InvalidBidException exception = assertThrows(InvalidBidException.class,
                () -> bidServiceImpl.placeBid(1L, BigDecimal.valueOf(100), "bidder@test.com"));
        assertThat(exception.getMessage()).isEqualTo("Jesteś już najwyższym licytantem.");

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user has insufficient balance")
    void shouldThrowExceptionWhenUserHasInsufficientBalance() {
        // given
        User seller = User.builder().id(10L).build();
        User bidder = User.builder().id(1L).email("bidder@test.com").balance(BigDecimal.valueOf(100)).build();
        Auction auction = Auction.builder().id(1L).seller(seller).build();

        when(userRepository.findByEmail("bidder@test.com")).thenReturn(Optional.of(bidder));
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.empty());

        // when & then
        InvalidBidException exception = assertThrows(InvalidBidException.class,
                () -> bidServiceImpl.placeBid(1L, BigDecimal.valueOf(1000), "bidder@test.com"));
        assertThat(exception.getMessage()).isEqualTo("Niewystarczające saldo konta.");

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when auction is not active")
    void shouldThrowExceptionWhenAuctionIsNotActive() {
        // given
        User seller = User.builder().id(10L).build();
        User bidder = User.builder().id(1L).email("bidder@test.com").balance(BigDecimal.valueOf(1000)).build();
        Auction auction = Auction.builder().id(1L).seller(seller).status("FINISHED").build();

        when(userRepository.findByEmail("bidder@test.com")).thenReturn(Optional.of(bidder));
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.empty());

        // when & then
        InvalidBidException exception = assertThrows(InvalidBidException.class,
                () -> bidServiceImpl.placeBid(1L, BigDecimal.valueOf(100), "bidder@test.com"));
        assertThat(exception.getMessage()).isEqualTo("Aukcja nie jest aktywna lub już się zakończyła.");

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when auction has already ended")
    void shouldThrowExceptionWhenAuctionHasAlreadyEnded() {
        // given
        User seller = User.builder().id(10L).build();
        User bidder = User.builder().id(1L).email("bidder@test.com").balance(BigDecimal.valueOf(1000)).build();
        LocalDateTime endTime = LocalDateTime.now().minusDays(1);
        Auction auction = Auction.builder().id(1L).seller(seller).status("ACTIVE").endTime(endTime).build();

        when(userRepository.findByEmail("bidder@test.com")).thenReturn(Optional.of(bidder));
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.empty());

        // when & then
        InvalidBidException exception = assertThrows(InvalidBidException.class,
                () -> bidServiceImpl.placeBid(1L, BigDecimal.valueOf(100), "bidder@test.com"));
        assertThat(exception.getMessage()).isEqualTo("Aukcja nie jest aktywna lub już się zakończyła.");

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when first bid is too low")
    void shouldThrowExceptionWhenFirstBidIsTooLow() {
        // given
        User seller = User.builder().id(10L).build();
        User bidder = User.builder().id(1L).email("bidder@test.com").balance(BigDecimal.valueOf(1000)).build();
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        Auction auction = Auction.builder()
                .id(1L)
                .seller(seller)
                .startPrice(BigDecimal.valueOf(1000))
                .currentPrice(BigDecimal.valueOf(1000))
                .status("ACTIVE")
                .endTime(endTime)
                .build();

        when(userRepository.findByEmail("bidder@test.com")).thenReturn(Optional.of(bidder));
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.empty());

        // when & then
        InvalidBidException exception = assertThrows(InvalidBidException.class,
                () -> bidServiceImpl.placeBid(1L, BigDecimal.valueOf(100), "bidder@test.com"));
        assertThat(exception.getMessage()).isEqualTo(
                "Pierwsza oferta musi wynosić co najmniej " + BigDecimal.valueOf(1000));

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when bid increment is too low")
    void shouldThrowExceptionWhenBidIncrementIsTooLow() {
        // given
        User seller = User.builder().id(10L).build();
        User bidder = User.builder().id(1L).email("bidder@test.com").balance(BigDecimal.valueOf(2000)).build();
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        Auction auction = Auction.builder()
                .id(1L)
                .seller(seller)
                .startPrice(BigDecimal.valueOf(1000))
                .currentPrice(BigDecimal.valueOf(1100))
                .minIncrement(BigDecimal.valueOf(100))
                .status("ACTIVE")
                .endTime(endTime)
                .build();

        User topBidUser = User.builder().id(2L).build();
        Bid topBid = Bid.builder().user(topBidUser).build();

        when(userRepository.findByEmail("bidder@test.com")).thenReturn(Optional.of(bidder));
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.of(topBid));

        // when & then
        InvalidBidException exception = assertThrows(InvalidBidException.class,
                () -> bidServiceImpl.placeBid(1L, BigDecimal.valueOf(1150), "bidder@test.com"));
        assertThat(exception.getMessage()).isEqualTo("Oferta musi być co najmniej " + auction.getMinIncrement() +
                " wyższa od aktualnej ceny.");

        verify(bidRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should save bid successfully when no previous bidder")
    void shouldSaveBidSuccessfullyWhenNoPreviousBidder() {
        // given
        User seller = User.builder().id(10L).build();
        User bidder = User.builder().id(1L).email("bidder@test.com").balance(BigDecimal.valueOf(1000)).build();
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        Auction auction = Auction.builder()
                .id(1L)
                .seller(seller)
                .startPrice(BigDecimal.valueOf(1000))
                .currentPrice(BigDecimal.valueOf(1000))
                .status("ACTIVE")
                .endTime(endTime)
                .build();

        LocalDateTime now = LocalDateTime.now();
        Bid savedBid = Bid.builder()
                .id(1L)
                .auction(auction)
                .user(bidder)
                .amount(BigDecimal.valueOf(1000))
                .createdAt(now)
                .build();

        when(userRepository.findByEmail("bidder@test.com")).thenReturn(Optional.of(bidder));
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.empty());
        when(bidRepository.save(any())).thenReturn(savedBid);

        // when
        PlaceBidResult result = bidServiceImpl.placeBid(1L, BigDecimal.valueOf(1000), "bidder@test.com");
        BidDto resultDto = result.getSavedBid();

        // then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(1000));
        assertThat(resultDto.getCreatedAt()).isNotNull();
        assertThat(resultDto.getAuction().getId()).isEqualTo(1L);
        assertThat(resultDto.getUser().getId()).isEqualTo(1L);
        assertThat(bidder.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getOutbidUserEmail()).isNull();

        verify(bidRepository).save(any(Bid.class));
        verify(userRepository).save(any(User.class));
        verify(auctionRepository).save(any(Auction.class));
    }

    @Test
    @DisplayName("Should refund previous bidder when top bidder exists")
    void shouldRefundPreviousBidderWhenTopBidderExists() {
        // given
        User seller = User.builder().id(10L).build();
        User bidder = User.builder().id(1L).email("bidder@test.com").balance(BigDecimal.valueOf(1200)).build();
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        Auction auction = Auction.builder()
                .id(1L)
                .seller(seller)
                .startPrice(BigDecimal.valueOf(1000))
                .currentPrice(BigDecimal.valueOf(1000))
                .minIncrement(BigDecimal.valueOf(100))
                .status("ACTIVE")
                .endTime(endTime)
                .build();

        User topBidUser = User.builder().id(2L).email("topbidder@test.com").balance(BigDecimal.ZERO).build();
        Bid topBid = Bid.builder().user(topBidUser).amount(BigDecimal.valueOf(1000)).build();

        LocalDateTime now = LocalDateTime.now();
        Bid savedBid = Bid.builder()
                .id(1L)
                .auction(auction)
                .user(bidder)
                .amount(BigDecimal.valueOf(1100))
                .createdAt(now)
                .build();

        when(userRepository.findByEmail("bidder@test.com")).thenReturn(Optional.of(bidder));
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(bidRepository.findTopByAuctionIdOrderByAmountDesc(1L)).thenReturn(Optional.of(topBid));
        when(bidRepository.save(any())).thenReturn(savedBid);

        // when
        PlaceBidResult result = bidServiceImpl.placeBid(1L, BigDecimal.valueOf(1100), "bidder@test.com");
        BidDto resultDto = result.getSavedBid();

        // then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(1100));
        assertThat(bidder.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(topBidUser.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1000));
        assertThat(result.getOutbidUserEmail()).isEqualTo("topbidder@test.com");
        assertThat(result.getOutbidAmount()).isEqualByComparingTo(BigDecimal.valueOf(1000));

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
