package com.kazmierczak.daniel.car_auction_platform.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus status;

    public BaseException(String message, String errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
