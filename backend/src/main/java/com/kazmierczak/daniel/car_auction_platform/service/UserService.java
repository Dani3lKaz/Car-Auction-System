package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.exception.EmailAlreadyTakenException;
import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.mapper.UserMapper;
import com.kazmierczak.daniel.car_auction_platform.models.dto.user.CreateUserDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.user.SimpleUserDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.user.UserDTO;
import com.kazmierczak.daniel.car_auction_platform.repository.UserRepository;
import com.kazmierczak.daniel.car_auction_platform.models.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public List<SimpleUserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toSimpleDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getById(Long id) {
        Optional<User> result = userRepository.findById(id);
        if (result.isPresent()) {
            return userMapper.toDTO(result.get());
        } else {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
    }

    public SimpleUserDTO saveUser(CreateUserDTO userDto) {
        User user = userMapper.toEntity(userDto);
        if (user.getId() == null && userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException("User with email " + user.getEmail() + " already exists");
        }
        User savedUser = userRepository.save(user);
        return userMapper.toSimpleDTO(savedUser);
    }
}