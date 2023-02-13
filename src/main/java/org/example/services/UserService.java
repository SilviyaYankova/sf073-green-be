package org.example.services;

import org.example.models.entities.UserEntity;
import org.example.models.responses.*;

import java.util.List;
import java.util.Optional;

public interface UserService {
    RegisterUserResponse register(String name, String username, String password);
    boolean usernameAlreadyExists(String username);
    List<GetAllUsersResponse> getAllUsers();
    DeleteUserResponse delete(String username);
    Optional<UserEntity> findUserByUsername(String username);
    ChangeUserRoleResponse changeRole(String username, String userRole);
    ChangeUserAccessResponse changeAccess(String username, String operation);
    boolean checkIfRoleExist(String role);
    Optional<UserEntity> getLoggedUser();
}