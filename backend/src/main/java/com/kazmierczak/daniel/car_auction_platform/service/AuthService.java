package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dto.AuthResponse;
import com.kazmierczak.daniel.car_auction_platform.dto.LoginRequest;
import com.kazmierczak.daniel.car_auction_platform.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
