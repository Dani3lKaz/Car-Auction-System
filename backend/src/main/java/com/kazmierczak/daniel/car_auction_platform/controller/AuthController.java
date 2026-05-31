package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.dto.LoginRequest;
import com.kazmierczak.daniel.car_auction_platform.dto.RegisterRequest;
import com.kazmierczak.daniel.car_auction_platform.dto.UserDto;
import com.kazmierczak.daniel.car_auction_platform.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
