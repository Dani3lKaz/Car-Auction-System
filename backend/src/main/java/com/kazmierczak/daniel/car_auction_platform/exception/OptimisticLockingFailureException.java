package com.kazmierczak.daniel.car_auction_platform.exception;

import org.springframework.http.HttpStatus;

public class OptimisticLockingFailureException extends BaseException{
    public OptimisticLockingFailureException(String message) {
        super(message, "OPTIMISTIC_LOCK_FAILURE", HttpStatus.CONFLICT);
    }
}
