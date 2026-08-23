package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.models.dto.user.CreateUserDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.user.SimpleUserDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.user.UserDTO;
import com.kazmierczak.daniel.car_auction_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<SimpleUserDTO> findAll(){
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @PostMapping
    public ResponseEntity<SimpleUserDTO> addUser(@RequestBody CreateUserDTO userDto){
        SimpleUserDTO saved = userService.saveUser(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
