package com.kazmierczak.daniel.car_auction_platform.mapper;

import com.kazmierczak.daniel.car_auction_platform.model.dto.user.CreateUserDTO;
import com.kazmierczak.daniel.car_auction_platform.model.dto.user.SimpleUserDTO;
import com.kazmierczak.daniel.car_auction_platform.model.dto.user.UserDTO;
import com.kazmierczak.daniel.car_auction_platform.model.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .balance(user.getBalance())
                .role(user.getRole())
                .build();
    }

    public SimpleUserDTO toSimpleDTO(User user) {
        return SimpleUserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public User toEntity(CreateUserDTO createUserDTO) {
        return User.builder()
                .firstName(createUserDTO.getFirstName())
                .lastName(createUserDTO.getLastName())
                .email(createUserDTO.getEmail())
                .password(createUserDTO.getPassword())
                .balance(BigDecimal.ZERO)
                .build();
    }
}
