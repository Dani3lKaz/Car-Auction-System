package com.kazmierczak.daniel.car_auction_platform.exception;

import org.springframework.http.HttpStatus;

public class InvalidBidException extends BaseException{
    public InvalidBidException(String message) {
        super(message, "INVALID_BID", HttpStatus.BAD_REQUEST);
    }
}
