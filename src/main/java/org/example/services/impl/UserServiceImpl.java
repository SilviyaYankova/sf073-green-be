package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.models.entities.UserEntity;
import org.example.models.requests.RegisterUserRequest;
import org.example.models.responses.DeleteUserResponse;
import org.example.models.responses.RegisterUserResponse;
import org.example.repositories.UserRepository;
import org.example.services.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    @Override
    public RegisterUserResponse register(RegisterUserRequest request) {
        UserEntity userEntity = new UserEntity(request.getName(), request.getUsername());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(userEntity);
        return new RegisterUserResponse(userEntity.getId(), userEntity.getName(), userEntity.getUsername());
    }

    @Override
    public boolean usernameAlreadyExists(String username) {
        return userRepository
                .findByUsernameIgnoreCase(username)
                .isPresent();
    }

    @Override
    public List<RegisterUserResponse> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map((a) -> new RegisterUserResponse(a.getId(), a.getName(), a.getUsername()))
                .collect(Collectors.toList());
    }

    @Override
    public DeleteUserResponse delete(String username) {
        UserEntity user = userExists(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + "doesn't exist!"));
        userRepository.delete(user);
        return new DeleteUserResponse(user.getUsername(), "Deleted successfully!");
    }

    public Optional<UserEntity> userExists(String username) {
        return userRepository
                .findByUsernameIgnoreCase(username);
    }
}

