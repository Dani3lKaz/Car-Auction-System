package com.kazmierczak.daniel.car_auction_platform.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyTakenException extends BaseException {
    public  EmailAlreadyTakenException(String message) {
        super(message, "EMAIL_ALREADY_TAKEN", HttpStatus.CONFLICT);
    }
}
