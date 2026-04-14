package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.dto.UserDto;
import com.kazmierczak.daniel.car_auction_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final JsonMapper jsonMapper;

    @Autowired
    public UserRestController(UserService userService, JsonMapper jsonMapper){
        this.userService = userService;
        this.jsonMapper = jsonMapper;
    }

    @GetMapping
    public List<UserDto> findAll(){
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        UserDto userDto = userService.getById(userId);

        if(userDto == null){
            throw new RuntimeException("User id not found - " +  userId);
        }

        return userDto;
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto){
        userDto.setId(null);

        return userService.saveUser(userDto);
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UserDto userDto){
        return userService.saveUser(userDto);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId){
        UserDto userDto = userService.getById(userId);

        if(userDto == null){
            throw new RuntimeException("User id not found - " +  userId);
        }

        userService.deleteById(userId);

        return "Deleted user id - " + userId;
    }
}
