package com.kazmierczak.daniel.car_auction_platform.rest;

import com.kazmierczak.daniel.car_auction_platform.entity.User;
import com.kazmierczak.daniel.car_auction_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private UserService userService;
    private JsonMapper jsonMapper;

    @Autowired
    public UserRestController(UserService userService, JsonMapper jsonMapper){
        this.userService = userService;
        this.jsonMapper = jsonMapper;
    }

    @GetMapping("/users")
    public List<User> findAll(){
        return userService.getAll();
    }

    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable Long userId) {
        User user = userService.getById(userId);

        if(user == null){
            throw new RuntimeException("User id not found - " +  userId);
        }

        return user;
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user){
        user.setId(0L);

        return userService.saveUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable Long userId){
        User user = userService.getById(userId);

        if(user == null){
            throw new RuntimeException("User id not found - " +  userId);
        }

        userService.deleteById(userId);

        return "Deleted user id - " + userId;
    }
}
