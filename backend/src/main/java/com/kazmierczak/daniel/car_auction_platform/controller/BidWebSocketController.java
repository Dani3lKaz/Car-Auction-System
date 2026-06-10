package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.dto.BidDto;
import com.kazmierczak.daniel.car_auction_platform.dto.PlaceBidMessage;
import com.kazmierczak.daniel.car_auction_platform.dto.PlaceBidResult;
import com.kazmierczak.daniel.car_auction_platform.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class BidWebSocketController {

    private final BidService bidService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/bids/place")
    public void placeBid(PlaceBidMessage message, Principal principal) {
        if (principal == null) {
            return;
        }

        PlaceBidResult result = bidService.placeBid(
                message.getAuctionId(),
                message.getAmount(),
                principal.getName());

        messagingTemplate.convertAndSend(
                "/topic/auctions/" + message.getAuctionId() + "/bids",
                result.getSavedBid());

        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/bid-success",
                result.getSavedBid());

        if (result.getOutbidUserEmail() != null) {
            messagingTemplate.convertAndSendToUser(
                    result.getOutbidUserEmail(),
                    "/queue/outbid",
                    "Twoja oferta została przebita! Środki " + result.getOutbidAmount()
                            + " PLN wróciły na Twoje konto.");
        }
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Exception ex) {
        return ex.getMessage();
    }
}
