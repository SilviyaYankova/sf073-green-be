package org.example.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.models.requests.RegisterUserRequest;
import org.example.models.responses.DeleteUserResponse;
import org.example.models.responses.RegisterUserResponse;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class UserController {
    final UserService userService;

    @PostMapping("/user")
    ResponseEntity<RegisterUserResponse> register(@RequestBody @Valid RegisterUserRequest request) {
        if (userService.usernameAlreadyExists(request.getUsername())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    List<RegisterUserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/user/{username}")
    ResponseEntity<DeleteUserResponse> delete(@PathVariable String username) {
        if (userService.userExists(username).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.delete(username), HttpStatus.OK);
    }

}

