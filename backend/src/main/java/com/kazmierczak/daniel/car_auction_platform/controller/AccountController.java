package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.dto.UpdateAccountRequest;
import com.kazmierczak.daniel.car_auction_platform.dto.UpdateAccountResponse;
import com.kazmierczak.daniel.car_auction_platform.dto.UserDto;
import com.kazmierczak.daniel.car_auction_platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;

    @GetMapping
    public UserDto getAccount(Authentication authentication) {
        return userService.getByEmail(authentication.getName());
    }

    @PutMapping
    public UpdateAccountResponse updateAccount(@RequestBody UpdateAccountRequest request,
                                               Authentication authentication) {
        return userService.updateAccount(authentication.getName(), request);
    }
}
