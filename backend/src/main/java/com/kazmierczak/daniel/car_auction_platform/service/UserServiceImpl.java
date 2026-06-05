package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.exception.EmailAlreadyTakenException;
import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.repository.UserRepository;
import com.kazmierczak.daniel.car_auction_platform.dto.UpdateAccountRequest;
import com.kazmierczak.daniel.car_auction_platform.dto.UpdateAccountResponse;
import com.kazmierczak.daniel.car_auction_platform.dto.UserDto;
import com.kazmierczak.daniel.car_auction_platform.security.JwtService;
import com.kazmierczak.daniel.car_auction_platform.entity.User;
import com.kazmierczak.daniel.car_auction_platform.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$");
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


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
    public UserDto getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public UpdateAccountResponse updateAccount(String email, UpdateAccountRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Brak danych do zapisania.");
        }

        String firstName = request.getFirstName() != null ? request.getFirstName().trim() : "";
        String lastName = request.getLastName() != null ? request.getLastName().trim() : "";
        String newEmail = request.getEmail() != null ? request.getEmail().trim() : "";

        if (firstName.isBlank() || lastName.isBlank() || newEmail.isBlank()) {
            throw new IllegalArgumentException("Imię, nazwisko i e-mail są wymagane.");
        }

        if (!EMAIL_PATTERN.matcher(newEmail).matches()) {
            throw new IllegalArgumentException("Nieprawidłowy format adresu e-mail.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));

        boolean emailChanged = !newEmail.equalsIgnoreCase(user.getEmail());

        if (emailChanged && userRepository.findByEmail(newEmail).isPresent()) {
            throw new EmailAlreadyTakenException("Użytkownik z adresem " + newEmail + " już istnieje.");
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(newEmail);

        String newPassword = request.getNewPassword();
        if (newPassword != null && !newPassword.isBlank()) {
            String currentPassword = request.getCurrentPassword();
            if (currentPassword == null || currentPassword.isBlank()) {
                throw new IllegalArgumentException("Obecne hasło jest wymagane przy zmianie hasła.");
            }
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                throw new IllegalArgumentException("Nieprawidłowe obecne hasło.");
            }
            if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
                throw new IllegalArgumentException(
                        "Hasło musi mieć min. 8 znaków, min. 1 dużą literę i min. 1 cyfrę.");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        User savedUser = userRepository.save(user);
        UserDto userDto = UserMapper.toDto(savedUser);

        String token = emailChanged ? jwtService.generateToken(savedUser) : null;

        return UpdateAccountResponse.builder()
                .user(userDto)
                .token(token)
                .build();
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
