package com.kazmierczak.daniel.car_auction_platform.mapper;

import com.kazmierczak.daniel.car_auction_platform.models.dto.user.CreateUserDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.user.SimpleUserDTO;
import com.kazmierczak.daniel.car_auction_platform.models.dto.user.UserDTO;
import com.kazmierczak.daniel.car_auction_platform.models.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .balance(user.getBalance())
                .build();
    }

    public SimpleUserDTO toSimpleDTO(User user) {
        return SimpleUserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .balance(userDTO.getBalance())
                .build();
    }

    public User toEntity(CreateUserDTO createUserDTO) {
        return User.builder()
                .firstName(createUserDTO.getFirstName())
                .lastName(createUserDTO.getLastName())
                .email(createUserDTO.getEmail())
                .password(createUserDTO.getPassword())
                .build();
    }
}