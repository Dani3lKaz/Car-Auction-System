package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dto.UpdateAccountRequest;
import com.kazmierczak.daniel.car_auction_platform.dto.UpdateAccountResponse;
import com.kazmierczak.daniel.car_auction_platform.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();
    UserDto getById(Long id);
    UserDto getByEmail(String email);
    UpdateAccountResponse updateAccount(String email, UpdateAccountRequest request);
    UserDto saveUser(UserDto userDto);
    void deleteById(Long id);
}
