package org.example.services;

import org.example.models.entities.UserEntity;
import org.example.models.requests.RegisterUserRequest;
import org.example.models.responses.DeleteUserResponse;
import org.example.models.responses.RegisterUserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
    RegisterUserResponse register(RegisterUserRequest request);

    boolean usernameAlreadyExists(String username);

    List<RegisterUserResponse> getAllUsers();

    DeleteUserResponse delete(String username);

    Optional<UserEntity> userExists(String username);
}