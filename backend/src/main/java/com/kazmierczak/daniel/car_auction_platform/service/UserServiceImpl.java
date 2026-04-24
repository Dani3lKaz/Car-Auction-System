package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.exception.EmailAlreadyTakenException;
import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.repository.UserRepository;
import com.kazmierczak.daniel.car_auction_platform.dto.UserDto;
import com.kazmierczak.daniel.car_auction_platform.entity.User;
import com.kazmierczak.daniel.car_auction_platform.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long id) {
        Optional<User> result = userRepository.findById(id);
        if (result.isPresent()) {
            return UserMapper.toDto(result.get());
        } else {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        if (user.getId() == null && userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException("User with email " + user.getEmail() + " already exists");
        }
        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

    @Override
    public void deleteById(Long id) {
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("Cannot delete. User with id " + id + " not found.");
        }
        userRepository.deleteById(id);
    }
}
