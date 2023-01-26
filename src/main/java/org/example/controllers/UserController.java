package org.example.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.models.entities.UserEntity;
import org.example.models.enums.RoleEnum;
import org.example.models.requests.ChangeUserAccessRequest;
import org.example.models.requests.ChangeUserRoleRequest;
import org.example.models.requests.RegisterUserRequest;
import org.example.models.responses.ChangeUserAccessResponse;
import org.example.models.responses.ChangeUserRoleResponse;
import org.example.models.responses.DeleteUserResponse;
import org.example.models.responses.RegisterUserResponse;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class UserController {
    final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user")
    RegisterUserResponse register(@RequestBody @Valid RegisterUserRequest request) {
        if (userService.usernameAlreadyExists(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return userService.register(request);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPPORT')")
    @GetMapping("/list")
    List<RegisterUserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/user/{username}")
    DeleteUserResponse delete(@PathVariable String username) {
        if (userService.findUserByUsername(username).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return userService.delete(username);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/access")
    ChangeUserAccessResponse access(@RequestBody ChangeUserAccessRequest request) {
        ChangeUserAccessResponse changeUserAccessResponse = userService.changeAccess(request);
        return new ChangeUserAccessResponse(changeUserAccessResponse.getStatus());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/role")
    ChangeUserRoleResponse role(@RequestBody ChangeUserRoleRequest request) {
        if (!userService.checkIfRoleExist(request.getRole())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (request.getRole().equals(RoleEnum.ADMINISTRATOR.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<UserEntity> userEntity = userService.findUserByUsername(request.getUsername());
        if (userEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity user = userEntity.get();
        if (request.getRole().equals(userEntity.get().getRole().name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        userService.changeRole(request);
        return new ChangeUserRoleResponse(user.getId(), user.getName(),
                                          user.getUsername(), user.getRole().toString());
    }
}

