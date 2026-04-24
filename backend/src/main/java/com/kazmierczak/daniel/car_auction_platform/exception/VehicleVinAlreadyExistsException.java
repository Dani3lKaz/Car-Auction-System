package com.kazmierczak.daniel.car_auction_platform.exception;

import org.springframework.http.HttpStatus;

public class VehicleVinAlreadyExistsException extends BaseException{
    public VehicleVinAlreadyExistsException(String message) {
        super(message, "VEHICLE_VIN_EXISTS", HttpStatus.CONFLICT);
    }
}
