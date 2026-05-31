package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dto.LoginRequest;
import com.kazmierczak.daniel.car_auction_platform.dto.RegisterRequest;
import com.kazmierczak.daniel.car_auction_platform.dto.UserDto;
import com.kazmierczak.daniel.car_auction_platform.entity.User;
import com.kazmierczak.daniel.car_auction_platform.exception.EmailAlreadyTakenException;
import com.kazmierczak.daniel.car_auction_platform.exception.InvalidCredentialsException;
import com.kazmierczak.daniel.car_auction_platform.mapper.UserMapper;
import com.kazmierczak.daniel.car_auction_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException("User with email " + request.getEmail() + " already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .balance(BigDecimal.ZERO)
                .build();

        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return UserMapper.toDto(user);
    }
}
