package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dao.UserRepository;
import com.kazmierczak.daniel.car_auction_platform.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        Optional<User> result = userRepository.findById(id);

        User user = null;

        if(result.isPresent()) {
            user = result.get();
        }else{
            throw new RuntimeException("Did not find user id - " + id);
        }

        return user;
    }
}
