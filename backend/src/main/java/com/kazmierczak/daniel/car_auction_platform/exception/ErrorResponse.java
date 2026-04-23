package com.kazmierczak.daniel.car_auction_platform.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String errorCode;
    private int status;
    private Instant timestamp;
    private String path;
}
