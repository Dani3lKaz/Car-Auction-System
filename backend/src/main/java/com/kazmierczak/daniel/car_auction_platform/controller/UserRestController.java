package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.dto.UserDto;
import com.kazmierczak.daniel.car_auction_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> findAll(){
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UserDto userDto){
        return userService.saveUser(userDto);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId){
        userService.deleteById(userId);
        return "Deleted user id - " + userId;
    }
}
