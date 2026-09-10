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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


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
        user.setRole("ROLE_USER");
        if (user.getId() == null && userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException("User with email " + user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toSimpleDTO(savedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + username + " not found"));
    }
}