package com.kazmierczak.daniel.car_auction_platform.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException{
    public  ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
