package org.example.services;

import org.example.models.entities.UserEntity;
import org.example.models.responses.ChangeUserAccessResponse;
import org.example.models.responses.ChangeUserRoleResponse;
import org.example.models.responses.DeleteUserResponse;
import org.example.models.responses.RegisterUserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
    RegisterUserResponse register(String name, String username, String password);
    boolean usernameAlreadyExists(String username);
    List<RegisterUserResponse> getAllUsers();
    DeleteUserResponse delete(String username);
    Optional<UserEntity> findUserByUsername(String username);
    ChangeUserRoleResponse changeRole(String username, String userRole);
    ChangeUserAccessResponse changeAccess(String username, String operation);
    boolean checkIfRoleExist(String role);
    Optional<UserEntity> getLoggedUser();
}