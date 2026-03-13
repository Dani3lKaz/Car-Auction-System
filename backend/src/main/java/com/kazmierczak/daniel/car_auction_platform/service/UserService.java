package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(Long id);

}
