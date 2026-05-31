package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dto.LoginRequest;
import com.kazmierczak.daniel.car_auction_platform.dto.RegisterRequest;
import com.kazmierczak.daniel.car_auction_platform.dto.UserDto;

public interface AuthService {
    UserDto register(RegisterRequest request);
    UserDto login(LoginRequest request);
}
