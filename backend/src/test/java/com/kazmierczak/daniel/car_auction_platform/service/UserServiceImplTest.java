package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dto.UserDto;
import com.kazmierczak.daniel.car_auction_platform.entity.User;
import com.kazmierczak.daniel.car_auction_platform.exception.EmailAlreadyTakenException;
import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.mapper.UserMapper;
import com.kazmierczak.daniel.car_auction_platform.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Implementation Test")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("Should return user when user exists")
    void shouldReturnUserWhenUserExists() {
        // given
        long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .firstName("User")
                .lastName("Test")
                .build();
        UserDto expectedDto = UserMapper.toDto(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        UserDto resultDto = userServiceImpl.getById(userId);

        // then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(expectedDto.getId());
        assertThat(resultDto.getEmail()).isEqualTo(expectedDto.getEmail());
        assertThat(resultDto.getFirstName()).isEqualTo(expectedDto.getFirstName());
    }

    @Test
    @DisplayName("Should throw exception when user id not found")
    void shouldThrowExceptionWhenUserIdNotFound() {
        // given
        long nonExistentId = 99L;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> userServiceImpl.getById(nonExistentId));
        assertThat(exception.getMessage()).isEqualTo("User with id " + nonExistentId + " not found");
    }

    @Test
    @DisplayName("Should successfully save user when valid")
    void shouldSaveUserSuccessfully() {
        // given
        UserDto inputDto = UserDto.builder()
                .email("new@example.com")
                .firstName("User")
                .lastName("Test")
                .build();

        User savedUser = User.builder()
                .id(1L)
                .email("new@example.com")
                .firstName("User")
                .lastName("Test")
                .build();

        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        UserDto resultDto = userServiceImpl.saveUser(inputDto);

        // then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(1L);
        assertThat(resultDto.getEmail()).isEqualTo("new@example.com");

        verify(userRepository).findByEmail("new@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when saving new user with existing email")
    void shouldThrowExceptionWhenSavingUserWithExistingEmail() {
        // given
        UserDto inputDto = UserDto.builder()
                .email("existing@example.com")
                .firstName("User")
                .build();

        User existingUser = User.builder()
                .id(2L)
                .email("existing@example.com")
                .build();

        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));

        // when & then
        EmailAlreadyTakenException exception = assertThrows(EmailAlreadyTakenException.class,
                () -> userServiceImpl.saveUser(inputDto));
        assertThat(exception.getMessage()).isEqualTo("User with email existing@example.com already exists");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should successfully delete user when user exists")
    void shouldDeleteUserWhenUserExists() {
        // given
        long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        // when
        userServiceImpl.deleteById(userId);

        // then
        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non existing user")
    void shouldThrowExceptionWhenDeletingNonExistingUser() {
        // given
        long nonExistentId = 99L;
        when(userRepository.existsById(nonExistentId)).thenReturn(false);

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> userServiceImpl.deleteById(nonExistentId));
        assertThat(exception.getMessage()).isEqualTo("Cannot delete. User with id " + nonExistentId + " not found.");

        verify(userRepository, never()).deleteById(nonExistentId);
    }
}
