package org.example.controllers;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.models.entities.UserEntity;
import org.example.models.requests.ChangeUserAccessRequest;
import org.example.models.requests.ChangeUserRoleRequest;
import org.example.models.requests.LoginUserRequest;
import org.example.models.requests.RegisterUserRequest;
import org.example.models.responses.*;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class UserController {
    final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user")
    RegisterUserResponse register(@RequestBody @Valid RegisterUserRequest request) {
        return userService.register(request.getName(), request.getUsername(), request.getPassword());
    }

    @PostMapping("/login")
    ResponseEntity<LoginUserResponse> login(@RequestBody LoginUserRequest request) {
        Optional<UserEntity> userEntity = userService.findUserByUsername(request.getUsername());
        if(userEntity.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LoginUserResponse loginUserResponse = new LoginUserResponse(
                userEntity.get().getId(),
                userEntity.get().getName(),
                userEntity.get().getUsername(),
                request.getPassword(),
                userEntity.get().getRole().name(),
                userEntity.get().isAccountLocked()
        );
        return new ResponseEntity<>(loginUserResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPPORT')")
    @GetMapping("/list")
    List<GetAllUsersResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/user/{username}")
    DeleteUserResponse delete(@PathVariable String username) {
        return userService.delete(username);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/access")
    ChangeUserAccessResponse access(@RequestBody ChangeUserAccessRequest request) {
        ChangeUserAccessResponse response = userService.changeAccess(request.getUsername(), request.getOperation());
        return new ChangeUserAccessResponse(response.getStatus());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/role")
    ChangeUserRoleResponse role(@RequestBody ChangeUserRoleRequest request) {
        return userService.changeRole(request.getUsername(), request.getRole());
    }
}

