package org.example.controllers;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.models.requests.ChangeUserAccessRequest;
import org.example.models.requests.RegisterUserRequest;
import org.example.models.responses.ChangeUserAccessResponse;
import org.example.models.responses.DeleteUserResponse;
import org.example.models.responses.RegisterUserResponse;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/**")
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

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPPORT')")
    @GetMapping("/list")
    List<RegisterUserResponse> getAllUsers() {
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
}

